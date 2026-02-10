using HOTELINTERFAZ.ViewModels;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace HOTELINTERFAZ.Views
{
    /// <summary>
    /// Lógica de interacción para EmpleadosView.xaml
    /// </summary>
    public partial class EmpleadosView : UserControl
    {
        private readonly EmpleadosViewModel _vm = new();
        private readonly ICollectionView _view;
        public EmpleadosView()
        {
            InitializeComponent();
            DataContext = new EmpleadosViewModel();

            _view = CollectionViewSource.GetDefaultView(DgEmpleados.ItemsSource);
            
        }

        private void Editar_Empleado_Click(object sender, RoutedEventArgs e)
        {

        }

        private void Nuevo_Empleado_Click(object sender, RoutedEventArgs e)
        {

        }

        private void Eliminar_Empleado_Click(object sender, RoutedEventArgs e)
        {

        }

        private void Buscar_Empleado_Click(object sender, RoutedEventArgs e)
        {

        }

        private void DgEmpleados_RowEditEnding(object sender, DataGridRowEditEndingEventArgs e)
        {

        }
    }
}
