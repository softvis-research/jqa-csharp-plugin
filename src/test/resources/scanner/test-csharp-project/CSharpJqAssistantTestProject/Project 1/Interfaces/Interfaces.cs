namespace Project_1.Interfaces
{
    interface ChildInterface: ParentInterface
    {
        TestMethod(Dependency1 dpe);
    }

    interface ParentInterface
    {
        FooBar(Dependency1 dpee);
    }
}
