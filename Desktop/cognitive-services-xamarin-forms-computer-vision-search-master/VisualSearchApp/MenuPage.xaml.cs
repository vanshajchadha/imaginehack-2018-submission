using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace VisualSearchApp
{
    public partial class MenuPage : ContentPage
    {
        public MenuPage()
        {
            InitializeComponent();
        }

        private async void NavigateButton1_OnClicked(object sender, EventArgs e) {  
            await Navigation.PushModalAsync(new OcrSelectPage()); 
        }  

        private async void NavigateButton2_OnClicked(object sender, EventArgs e) {  
            await Navigation.PushModalAsync(new OcrSelectPage()); 
        } 


 


    }
}
