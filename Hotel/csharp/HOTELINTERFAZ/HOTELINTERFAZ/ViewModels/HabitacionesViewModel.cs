using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using HOTELINTERFAZ.Models;
using System.Windows;
using System.Net.Http.Json;

namespace HOTELINTERFAZ.ViewModels
{
    public class HabitacionesViewModel
    {
        public ObservableCollection<Habitacion> Habitaciones { get; } = new();
        private readonly HttpClient _client;
        public HabitacionesViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };
            _ = CargarHabitaciones();
        }
        public async Task CargarHabitaciones()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Habitacion>>("habitaciones");
                Habitaciones.Clear();
                if (lista != null)
                {
                    foreach (var h in lista)
                        Habitaciones.Add(h);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error API (GET):\n" + ex.ToString());
            }
        }
        public async Task<Habitacion?> CrearHabitacionAsync(Habitacion h)
        {
            try
            {
                var resp = await _client.PostAsJsonAsync("habitaciones", h);
                if (!resp.IsSuccessStatusCode)
                {
                    var body = await resp.Content.ReadAsStringAsync();
                    MessageBox.Show($"Error API (POST) {(int)resp.StatusCode} {resp.ReasonPhrase}\n\n{body}");
                    return null;
                }

                return await resp.Content.ReadFromJsonAsync<Habitacion>();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error API (POST):\n" + ex.ToString());
                return null;
            }
        }
        public async Task<bool> ActualizarHabitacionAsync(Habitacion h)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(h.Id))
                    return false;
                var resp = await _client.PutAsJsonAsync($"habitaciones/{h.Id}", h);
                resp.EnsureSuccessStatusCode();
                return true;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error API (PUT):\n" + ex.ToString());
                return false;
            }
        }
        public async Task<bool> EliminarHabitacionAsync(Habitacion h)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(h.Id))
                    return false;

                var resp = await _client.DeleteAsync($"habitaciones/{h.Id}");
                resp.EnsureSuccessStatusCode();
                return true;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error API (DELETE):\n" + ex.ToString());
                return false;
            }
        }
    }
}
