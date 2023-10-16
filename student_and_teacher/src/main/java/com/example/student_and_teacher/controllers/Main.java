import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int n = in.nextInt();

        while (n --> 0) {

            int f_l = in.nextInt();
            int s_l = in.nextInt();
            int i = 0, l = 0;

            String f = in.next();
            String s = in.next();
            int cycle = 1;
            boolean res = true;
            while (res) {
                if (f.charAt(i) == s.charAt(l)) {
                    i++;
                    l++;
                }
                else {
                    if(l > 0){
                        res = false;
                        break;
                    }
                    else i++;
                }

                if(!res) break;

                if (l == s_l) break;

                if (i == f_l){
                    i = 0;
                    cycle++;
                }
            }

            System.out.println(cycle + " : "+ res);
        }
    }
}