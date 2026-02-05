using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace HOTELINTERFAZ.Models
{
    public class LogInResponse
    {
        public class LoginResponse
        {
            public string token { get; set; }
            public Usuario usuario { get; set; }
        }
    }
}
