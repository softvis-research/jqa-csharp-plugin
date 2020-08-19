
class CyclomaticComplexityExample {

    void shouldBeOne(int a, int b ) {

        return a + b;
    }

    void shouldBeTwo(bool a ) {

        int x = 0;

        if (a) {
            x=1;
        } else {
            x=2;
        }
    }

    void shouldBeTwoAsWell() {

        int x = 0;

        for(int i = 0; i < 10; i++) {
            x++;
        }
    }

    void shouldBeThree(bool a , bool b ) {

        int x = 0;

        if (a && b) {
            x=1;
        } else {
            x=2;
        }
    }

}