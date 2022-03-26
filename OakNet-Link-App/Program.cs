using Chromely.Core;
using Chromely.Core.Configuration;
using Chromely.Core.Infrastructure;
using Chromely.Core.Network;
using System.Collections.Generic;
using System.Threading;

namespace OakNetLink.App
{
    class Program
    {
        static void Main(string[] args)
        {
            
            // create a configuration with OS-specific defaults
            var config = DefaultConfiguration.CreateForRuntimePlatform();

            // your configuration
            config.StartUrl = "local://app/OakNetLink.html";
            config.WindowOptions.Title = "OakNet Link";
            config.WindowOptions.RelativePathToIconFile = "favicon.ico";
            config.CefDownloadOptions.AutoDownloadWhenMissing = false; 


            // basic example of the application builder
            AppBuilder
            .Create(args)
            .UseApp<OakNetLinkApp>()
            .UseConfig<DefaultConfiguration>(config)
            .Build()
            .Run();
        }
    }
}
