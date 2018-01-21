using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace VisualSearchApp
{
    public partial class LoginPage : ContentPage
    {
        public LoginPage()
        {
            InitializeComponent();
        }

        async void TryToLogin(object sender, EventArgs e)
        {
     
            if (username.Text=="a"&&password.Text=="a"){
                await Navigation.PushModalAsync(new MenuPage());
            }
            else
            {
                await DisplayAlert("Error", "One or more of your keys are invalid. Please update them and try again", "OK");
            }
        }



    }
}
