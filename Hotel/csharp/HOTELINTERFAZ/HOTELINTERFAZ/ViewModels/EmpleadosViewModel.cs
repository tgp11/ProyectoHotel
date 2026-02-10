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
    public class EmpleadosViewModel
    {
        public ObservableCollection<Empleado> Empleados { get; set; } = new();

        private readonly HttpClient _client;

        public EmpleadosViewModel()
        {
            _client = new HttpClient
            {
                BaseAddress = new Uri("http://localhost:3000/")
            };
            _ = CargarEmpleados();
        }

        public async Task CargarEmpleados()
        {
            try
            {
                var lista = await _client.GetFromJsonAsync<List<Empleado>>("empleado");


                Empleados.Clear();

                foreach (var emp in lista)
                    Empleados.Add(emp);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
