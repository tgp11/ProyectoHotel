using HOTELINTERFAZ.Models;
using HOTELINTERFAZ.Ventanas;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Windows;

namespace HOTELINTERFAZ.ViewModels
{
    public class ReservasViewModel : INotifyPropertyChanged
    {
        public ObservableCollection<Reserva> Reservas { get; } = new();

        private List<Reserva> _todasReservas = new();

        private readonly HttpClient _client;

        public ReservasViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };

            _ = CargarReservas();
        }

        // ===============================
        // CARGA DE DATOS
        // ===============================
        public async Task CargarReservas()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Reserva>>("reservas");

                _todasReservas = lista ?? new List<Reserva>();

                AplicarFiltro();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de API: " + ex.Message);
            }
        }

        // ===============================
        // FILTRO
        // ===============================
        private bool _filtrarCanceladas;
        public bool FiltrarCanceladas
        {
            get => _filtrarCanceladas;
            set
            {
                if (_filtrarCanceladas != value)
                {
                    _filtrarCanceladas = value;
                    OnPropertyChanged(nameof(FiltrarCanceladas));
                    AplicarFiltro();
                }
            }
        }


        private Reserva _reservaSeleccionada;
        public Reserva ReservaSeleccionada
        {
            get => _reservaSeleccionada;
            set
            {
                _reservaSeleccionada = value;
                OnPropertyChanged(nameof(ReservaSeleccionada));
            }
        }

        public async Task EliminarReserva(string id)
        {
            try
            {
                var response = await _client.DeleteAsync($"reservas/{id}");

                if (!response.IsSuccessStatusCode)
                {
                    MessageBox.Show("No se pudo eliminar la reserva.");
                    return;
                }

                MessageBox.Show("Reserva eliminada correctamente.");

                await CargarReservas(); // refresca la tabla
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error eliminando reserva: " + ex.Message);
            }
        }



        private void AplicarFiltro()
        {
            Reservas.Clear();

            var listaFiltrada = _filtrarCanceladas
                ? _todasReservas.Where(r => !r.Cancelacion)
                : _todasReservas;

            foreach (var r in listaFiltrada)
                Reservas.Add(r);
        }

        public async Task<bool> AgregarReservaAsync(Reserva reserva)
        {
            try
            {
                var response = await _client.PostAsJsonAsync("reservas", reserva);
                if (response.IsSuccessStatusCode)
                {
                    await CargarReservas();
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> ActualizarReservaAsync(Reserva reserva)
        {
            try
            {
                // Enviar PUT a la API con la reserva actualizada
                var response = await _client.PutAsJsonAsync($"reservas/{reserva.Id}", reserva);

                if (response.IsSuccessStatusCode)
                {
                    // Recargar la lista de reservas
                    await CargarReservas();
                    return true;
                }

                return false;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al actualizar reserva: " + ex.Message);
                return false;
            }
        }

        public async Task<bool> CancelarReservaAsync(string id)
        {
            try
            {
                var response = await _client.PutAsync($"reservas/{id}/cancelar", null);
                if (response.IsSuccessStatusCode)
                {
                    await CargarReservas(); // Refresca la lista después de cancelar
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }





        // ===============================
        // INotifyPropertyChanged
        // ===============================
        public event PropertyChangedEventHandler PropertyChanged;

        protected void OnPropertyChanged(string propertyName)
            => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
    }

    

}
