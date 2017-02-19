using Foundation;
using System;
using UIKit;

namespace SimpleTest
{
    public partial class HomeViewController : UITableViewController
    {
        private HomeDataSource DataSource = new HomeDataSource(true);

        public HomeViewController (IntPtr handle) : base (handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            HomeCustomData[] items = new HomeCustomData[] {
                                                            new HomeCustomData("����", "����"),
                                                            new HomeCustomData("����", "����˵��"),
                                                            new HomeCustomData("Ӣ��", "Ӣ����˵��"),
                                                            new HomeCustomData("����", "�ձ���˵��"),
                                                            new HomeCustomData("Lisp", "�෶ʽ�������"),
                                                            new HomeCustomData("JAVA", "��ƽ̨��oop����"),
                                                            new HomeCustomData("C#", "΢������oop����")};

            DataSource.Add("��", items[0]);
            DataSource.Add("Human", items[1]);
            DataSource.Add("Human", items[2]);
            DataSource.Add("Human", items[3]);
            DataSource.Add("Computer", items[4]);
            DataSource.Add("Computer", items[5]);
            DataSource.Add("Computer", items[6]);

            /*
                        DataSource.Add("��������", "����");
                        DataSource.Add("��������", "Ӣ��");
                        DataSource.Add("��������", "����");
                        DataSource.Add("��������", "Lisp");
                        DataSource.Add("��������", "JAVA");
                        DataSource.Add("��������", "C#");
             */

            this.TableView.DataSource = DataSource;
        }

        public override nfloat GetHeightForRow(UITableView tableView, NSIndexPath indexPath)
        {
            if (DataSource.Get(indexPath) is HomeCustomData)
            {
                return 64;
            }
            return base.GetHeightForRow(tableView, indexPath);
        }

        public override void RowSelected(UITableView tableView, NSIndexPath indexPath)
        {
            if (DataSource.Get(indexPath) is HomeCustomData)
            {
                if (((HomeCustomData)DataSource.Get(indexPath)).Title.Equals("����"))
                {
                    Console.WriteLine("��������ͼ������");
                }
            }
            Console.WriteLine("clicked me...");
        }
    }
}