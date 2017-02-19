using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.IO;
using Mono.Data.Sqlite;

namespace SimpleUI.UIComponent
{
    class SimpleSqlite
    {
        string rootPath;
        string prefix;

        public SimpleSqlite(string root)
        {
            switch (root)
            {
                case "personal":
                case "Personal": rootPath = Environment.GetFolderPath(Environment.SpecialFolder.Personal); break;
                case "desktop":
                case "Desktop": rootPath = Environment.GetFolderPath(Environment.SpecialFolder.Desktop); break;
                case "system":
                case "System": rootPath = Environment.GetFolderPath(Environment.SpecialFolder.System); break;
                default:
                    rootPath = null;
                    Console.WriteLine("root error!");
                    break;
            }
            if (rootPath != null)
            {
                rootPath += "/";
            }
            Console.WriteLine(rootPath);
        }

        public SimpleSqlite(string root, string aPrefix) : this(root)
        {
            prefix = aPrefix;
            if (!prefix[prefix.Length - 1].Equals('/'))
            {
                prefix = prefix + "/";
            }
        }

        public static bool CreateDatabase(string root, string prefix, string databaseName)
        {
            return new SimpleSqlite(root, prefix).CreateDatabase(databaseName);
        }

        public static bool CreateTable(string root, string prefix, string databaseName, string tableName, Dictionary<string, string> FieldTypes)
        {
            return new SimpleSqlite(root, prefix).CreateTable(databaseName, tableName, FieldTypes);
        }

        public static bool InsertInto(string root, string prefix, string databaseName, string tableName, List<string> items)
        {
            return new SimpleSqlite(root, prefix).InsertInto(databaseName, tableName, items);
        }

        public static bool InsertInto(string root, string prefix, string databaseName, string tableName, Dictionary<string, string> items)
        {
            return new SimpleSqlite(root, prefix).InsertInto(databaseName, tableName, items);
        }

        public static List<Dictionary<string, string>> QueryFrom(string root, string prefix, string databaseName, string tableName, List<string> Fields, string Conditions)
        {
            return new SimpleSqlite(root, prefix).QueryFrom(databaseName, tableName, Fields, Conditions);
        }

        public bool CreateDatabase(string databaseName)
        {
            string Absolute = rootPath + prefix + databaseName;
            if (File.Exists(Absolute))
            {
                return false;
            }
            else
            {
                SqliteConnection.CreateFile(Absolute);
                return true;
            }
        }

        public void ExecuteNoQuery(string databaseName, string tableName, string command)
        {
            string Absolute = rootPath + prefix + databaseName;
            SqliteConnection sqlCon = new SqliteConnection(string.Format("Data source = {0}", Absolute));   //���������ݿ������
            sqlCon.Open();                  //������
            SqliteCommand sqlCom = new SqliteCommand(sqlCon);   //��sql������sqlCom�����ݿ�����
            sqlCom.CommandText = command;
            sqlCom.ExecuteNonQuery();       //ִ��sqlCom
            sqlCon.Close();
        }

        public List<Dictionary<string, string>> ExecuteQuery(string databaseName, string tableName, string command)
        {
            string Absolute = rootPath + prefix + databaseName;
            List<Dictionary<string, string>> result = new List<Dictionary<string, string>>();

            SqliteConnection sqlCon = new SqliteConnection(string.Format("Data source = {0}", Absolute));   //���������ݿ������
            sqlCon.Open();                                      //������
            SqliteCommand sqlCom = new SqliteCommand(sqlCon);   //��sql������sqlCom�����ݿ�����
            
            sqlCom.CommandText = command;
            SqliteDataReader reader = sqlCom.ExecuteReader();       //ִ��sqlCom
            while (reader.Read())
            {
                Dictionary<string, string> tmp = new Dictionary<string, string>();
                for (int i=0; i<reader.FieldCount; i++)
                {
                    tmp.Add(reader.GetName(i), reader.GetFieldValue<object>(i).ToString());
                }
                result.Add(tmp);
            }

            sqlCon.Close();
            return result;
        }

        public bool CreateTable(string databaseName, string tableName, Dictionary<string, string> FieldTypes)
        {
            string Absolute = rootPath + prefix + databaseName;
            if (File.Exists(Absolute))
            {
                string command = "CREATE TABLE "+tableName;
                command += "(";
                foreach (KeyValuePair<string, string> field_type in FieldTypes)
                {
                    command += String.Format("{0} {1},", field_type.Key, field_type.Value);
                }
                command = command.Substring(0, command.Length - 1);     //����һ������..
                command += ")";
                ExecuteNoQuery(databaseName, tableName, command);
                
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool InsertInto(string databaseName, string tableName, Dictionary<string, string> items)
        {
            string Absolute = rootPath + prefix + databaseName;
            if (File.Exists(Absolute))
            {
                string command = "INSERT INTO " + tableName;

                command += "(";
                foreach (KeyValuePair<string, string> item in items)
                {
                    command += item.Key + ",";
                }
                command = command.Substring(0, command.Length - 1);
                command += ") VALUES (";
                foreach (KeyValuePair<string, string> item in items)
                {
                    command += String.Format("'{0}',", item.Value);
                }
                command = command.Substring(0, command.Length - 1);
                command += ")";
                
                ExecuteNoQuery(databaseName, tableName, command);
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool InsertInto(string databaseName, string tableName, List<string> items)
        {
            string Absolute = rootPath + prefix + databaseName;
            if (File.Exists(Absolute))
            {
                int colNums = this.QueryColumnsNumber(databaseName, tableName);         //��������
//                items = items.GetRange(0, colNums);

                string command = String.Format("INSERT INTO {0} VALUES", tableName);
                
                command += "(";
                foreach (string item in items)
                {
                    command += String.Format("'{0}',", item);
                }
                command = command.Substring(0, command.Length - 1);
                command += ")";

                ExecuteNoQuery(databaseName, tableName, command);
                return true;
            }
            else
            {
                return false;
            }
        }

        public List<Dictionary<string, string>> QueryFrom(string databaseName, string tableName, List<string> Fields, string Conditions)
        {
            string Absolute = rootPath + prefix + databaseName;
            if (File.Exists(Absolute))
            {
                string FieldsString = "";
                string command = "";

                foreach (string Field in Fields)
                {
                    FieldsString += Field + ",";
                }
                FieldsString = FieldsString.Substring(0, FieldsString.Length - 1);

                if (Conditions == null)
                {
                    Conditions = "";        //Condition���пմ���null���滻
                }

                if (Conditions.Length == 0)
                {   //������
                    command = String.Format("SELECT {0} FROM {1}", FieldsString, tableName);
                }
                else
                {
                    command = String.Format("SELECT {0} FROM {1} WHERE {2}", FieldsString, tableName, Conditions);
                }
                

                return ExecuteQuery(databaseName, tableName, command);
            }
            else
            {
                return null;
            }
        }

        public int QueryColumnsNumber(string databaseName, string tableName)
        {   //����ָ�����ݿ� ָ���������
            return ExecuteQuery(databaseName, tableName, String.Format("PRAGMA table_info([{0}])", tableName)).Count;
        }
    }
}