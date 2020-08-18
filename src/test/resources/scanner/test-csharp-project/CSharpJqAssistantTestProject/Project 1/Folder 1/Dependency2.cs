using Project_1;

namespace Battle
{
    public class Dependency2 : MonoBehaviour
    {
        public const string const1 = "1", const2 = "foobar";
        public static string t = "4", test = "2";
        private float f = 3;
        private Dependency1 _dependency1;
        protected const float dep = 13;
        private static readonly ILog log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

    }
}
