
using Chromely;
using Chromely.Browser;
using Chromely.Core;
using Chromely.Core.Configuration;
using Chromely.Core.Infrastructure;
using Chromely.Core.Host;
using Chromely.Core.Logging;
using Chromely.NativeHosts;
using Chromely.Core.Network;
using Microsoft.Extensions.DependencyInjection;
using OakNetLink.App.Controllers;
using System;
using System.Threading;

namespace OakNetLink.App
{
    class OakNetLinkApp : ChromelyBasicApp
    {
        public override void ConfigureServices(IServiceCollection services)
        {
            base.ConfigureServices(services);
            RegisterChromelyControllerAssembly(services, typeof(OakNetLinkApp).Assembly);

        }  
    }
}