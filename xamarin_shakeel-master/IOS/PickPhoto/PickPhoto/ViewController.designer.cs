﻿// WARNING
//
// This file has been generated automatically by Xamarin Studio from the outlets and
// actions declared in your storyboard file.
// Manual changes to this file will not be maintained.
//
using Foundation;
using System;
using System.CodeDom.Compiler;
using UIKit;

namespace PickPhoto
{
    [Register ("ViewController")]
    partial class ViewController
    {
        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIButton btnSelect { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIImageView imgView { get; set; }

        void ReleaseDesignerOutlets ()
        {
            if (btnSelect != null) {
                btnSelect.Dispose ();
                btnSelect = null;
            }

            if (imgView != null) {
                imgView.Dispose ();
                imgView = null;
            }
        }
    }
}