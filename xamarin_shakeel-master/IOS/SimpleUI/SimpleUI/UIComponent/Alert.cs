using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UIKit;

namespace SimpleUI.UIComponent
{
    class SimpleAlert
    {
        public static void Show(string Title, string Message, string Button)
        {   //���� + ��Ϣ + ����ť + ���¼�
            var alert = new UIAlertView();
            alert.Title = Title;
            alert.Message = Message;
            alert.AddButton(Button);
            alert.Show();
        }

        public static void Show(string Title, string Message, string Button, EventHandler<UIButtonEventArgs> handler)
        {   // ���� + ��Ϣ + ����ť + �¼�����
            var alert = new UIAlertView();
            alert.Title = Title;
            alert.Message = Message;
            alert.AddButton(Button);
            alert.Dismissed += handler;
            alert.Show();
        }
        public static void Show(string Title, string Message, List<string> Buttons, EventHandler<UIButtonEventArgs> handler)
        {   // ���� + ��Ϣ + �ఴť + �¼�����
            var alert = new UIAlertView();
            nint index = 0;

            alert.Title = Title;
            alert.Message = Message;
            foreach (string Button in Buttons)
            {
                alert.AddButton(Button);
            }
            alert.Dismissed += handler;
            alert.Show();
        }

        public static nint BlockShow(string Title, string Message, List<string> Buttons, EventHandler<UIButtonEventArgs> handler)
        {   // ���� + ��Ϣ + �ఴť + �¼����� + ��ť�������� + ����
            var alert = new UIAlertView();
            int index = -1;

            alert.Title = Title;
            alert.Message = Message;
            foreach (string Button in Buttons)
            {
                alert.AddButton(Button);
            }
            alert.Dismissed += handler;
            alert.Dismissed += (sender, e) =>
            {
                index = (int)e.ButtonIndex;
            };
            alert.Show();
            return (nint)index;
        }
    }
}