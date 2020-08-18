using System;
using static System.Math;
using MyAlias = System.Collections.Generic;

namespace Project_1
{
    class Program
    {
        static void Main(string[] args)
        {
            Sqrt(9);
            
            Console.WriteLine("Hello World!");

            CallDep1();
            CallDep2();
        }

        private static void CallDep1()
        {
            new Dependency1().DoSomething();
        }

        private static float CallDep2()
        {
            Dependency1.DoSomething(2);
            return 7;
        }
    }
}