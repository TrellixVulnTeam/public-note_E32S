using System;
using System.Net;
using Independentsoft.Webdav;

namespace Sample
{
    class Program
    {
        static void Main(string[] args)
        {
            NetworkCredential credential = new NetworkCredential("admin", "admin");
            WebdavSession session = new WebdavSession(credential);
            Resource resource = new Resource(session);

            resource.CreateFolder("http://192.168.10.242/owncloud");
        }
    }
}

