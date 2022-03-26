using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace OakNetLink.App.JsonObjects
{
    class GamesHelper
    {
        public IList<Game> Games { get; set; }


        private static IList<Game> loadedGames;
        public static IList<Game> getGames()
        {
            // TODO Load Games from the web
            if (loadedGames == null)
                loadedGames = JsonSerializer.Deserialize<GamesHelper>(File.ReadAllText("./app/games/games.json")).Games;
            return loadedGames;
        }
    }
}
