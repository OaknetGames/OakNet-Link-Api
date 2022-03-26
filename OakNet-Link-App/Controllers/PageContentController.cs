using Chromely.Core.Configuration;
using Chromely.Core.Infrastructure;
using Chromely.Core.Network;
using OakNetLink.Api;
using OakNetLink.Api.Communication;
using OakNetLink.App.JsonObjects;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace OakNetLink.App.Controllers
{
    [ChromelyController(Name = "PageContentController")]
    public class PageContentController : ChromelyController
    {
        bool init = false;

        void initialize()
        {
            init = true;
            var thread = new Thread(() => {
                TunnelWrapper.prepare();

            });
            //thread.SetApartmentState(ApartmentState.STA);
            thread.Start();
        }


        [ChromelyRoute(Path = "/pagecontent/setCanonicalName")]
        public ChromelyResponse setCanonicalName(string name)
        {
            Logger.log("Got CanonicalName: " + name);
            TunnelWrapper.requestIp((string) name);
            return new ChromelyResponse();
        }

        [ChromelyRoute(Path = "/pagecontent/gamecards")]
        public ChromelyResponse getGameCards(ChromelyRequest request)
        {
            if (!init)
                initialize();

            var snippet = @$"
                <div class=""fade show active d-flex justify-content-center flex-wrap"" 
                 role=""tabpanel"" 
                 aria-labelledby=""ex1-tab-1"">
                {
                    string.Join("\n", GamesHelper.getGames().Select((game) =>
                    {
                        return $@"
                            <div class=""card m-2 hover-overlay"" 
                             style=""width: 300px; 
                             cursor: pointer; "" 
                             onclick=""gameCardPressed('{game.ID}', '{game.DisplayName}');"">
                                <img src=""games/{game.Path}splash.png""
                                 class=""card-img-top""
                                 alt=""..."" />
                                <div class=""card-body hover-zoom"">
                                    <h5 class=""card-title"" > {game.DisplayName}</h5>
                                    <p class=""card-text"">
                                        {game.Desc}
                                    </p>
                                </div>
                            <div class=""card mask"" style=""background-color: #a4a4a43f""></div>
                        </div>";
                    }))                   
                }
                </div>";
            var response = new ChromelyResponse();
            response.Data = snippet;
            return response;
        }

        [ChromelyRoute(Path = "/pagecontent/pollOakNetLinkStatus")]
        public ChromelyResponse pollOakNetLinkStatus(ChromelyRequest request)
        {
            var snippet = "";
            switch (ONL.MasterServer.EndPoint?.ConnectionState)
            {
                case ConnectionState.Connected:
                    snippet = "Connected to OakNet-Link";
                    break;
                case ConnectionState.Connecting:
                    var anim = "";
                    for (var i = 0; i < ((Environment.TickCount / 1000) % 3 + 1); i++)
                        anim += ".";
                    snippet = "Connecting to OakNet-Link" + anim;
                    break;
                case ConnectionState.Disconnected:
                    snippet = "Not connected to OakNet-Link";
                    break;
                default:
                    break;
            }

            var response = new ChromelyResponse();
            response.Data = snippet;
            return response;
        }

        [ChromelyRoute(Path = "/pagecontent/pollOakNetLinkLog")]
        public ChromelyResponse pollOakNetLinkLog(ChromelyRequest request)
        {
            var snippet = "";
            lock (TunnelWrapper.log)
            {
                foreach(var logEntry in TunnelWrapper.log.AsEnumerable().Reverse())
                {
                    snippet += logEntry + "\n";
                }
            }
            var response = new ChromelyResponse();
            response.Data = snippet;
            return response;
        }

        [ChromelyRoute(Path = "/pagecontent/myservers")]
        public ChromelyResponse getMyServers(ChromelyRequest request)
        {
            var snippet = @$"Nothing to see here!";
            var response = new ChromelyResponse();
            response.Data = snippet;
            return response;
        }
    }
}
