using Foundation;
using System;
using UIKit;

namespace CollectionView
{
    public partial class ImageCell : UICollectionViewCell
    {
        public const string CELLID = "ImageCell";
        public ImageCell(IntPtr handle) : base(handle)
        {
            this.Initialize();  //��ʼ������Ҫ�����cell�еĲ������е�
        }

        public UIImageView ImageView { get; private set; }

        public void Initialize()
        {
            this.ImageView = new UIImageView(this.ContentView.Bounds);              //��ʼ��һ��UIImageView��Ҳ����ͨ������ʵ�����UIImageView
            this.ImageView.ContentMode = UIViewContentMode.ScaleAspectFit;
            this.ContentView.AddSubview(this.ImageView);
        }
    }
}