using HOTELINTERFAZ.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Windows;

namespace HOTELINTERFAZ.ViewModels
{
    public class ClientesViewModel
    {
        // ObservableCollection que se ligará a la UI
        public ObservableCollection<ClienteReducido> Clientes { get; } = new ObservableCollection<ClienteReducido>();

        private readonly HttpClient _client;

        public ClientesViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };

            // Cargar clientes al iniciar por antonio
            _ = CargarClientesAsync();
        }

        public async Task CargarClientesAsync()
        {
            try
            {
                // ⚠ Ruta corregida: singular "cliente" para coincidir con tu backend
                var lista = await _client.GetFromJsonAsync<List<ClienteReducido>>("cliente");

                Clientes.Clear();

                if (lista != null)
                {
                    foreach (var c in lista)
                        Clientes.Add(c);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al cargar clientes: " + ex.Message);
            }
        }
    }
}
