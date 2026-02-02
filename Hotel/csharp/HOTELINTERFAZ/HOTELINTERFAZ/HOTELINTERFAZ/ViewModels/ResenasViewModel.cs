using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HOTELINTERFAZ.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Json;
using System.Windows;

namespace HOTELINTERFAZ.ViewModels
{
    public class ResenasViewModel
    {
        public ObservableCollection<Resena> Resenas { get; } = new();

        private readonly HttpClient _client = new()
        {
            BaseAddress = new Uri("http://localhost:3000/")
        };

        public ResenasViewModel()
        {
            _ = CargarResenas();
        }

        private async Task CargarResenas()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Resena>>("resenas");
                Resenas.Clear();
                foreach (var r in lista)
                    Resenas.Add(r);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de API: " + ex.Message);
            }
        }
    }
}
