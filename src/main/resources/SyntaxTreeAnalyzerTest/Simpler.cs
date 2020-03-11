using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace simple_coff
{
    class Program
    {
        public const double X = 1.0;
        public const double Y = 2.0;
        public const double Z = 3.0;

        public DateTime date;
        public int done = 1;
        public bool b = true;
        public string hello = "World";

        class Program2 {

        }
        static void notMain(string[] args)
        {
            int an_integer = 10;
            String a_string = "Hello";
     
        }
    }

    interface C<out X, in Y, Z>
    {
        X M(Y y);
        Z P { get; set; }
    }

    public partial class Employee
    {
        public DateTime DoWork(int number, int number2)
        {
            GoToLunch();
            var date1 = new DateTime(2008, 5, 1, 8, 30, 52);
            return date1;
        }
    }

    public partial class Employee
    {
        public void GoToLunch()
        {
        }
    }

    enum ErrorCode : ushort
    {
        None = 0,
        Unknown = 1,
        ConnectionLost = 100,
        OutlierReading = 200
    }

    delegate void D(int x);

}