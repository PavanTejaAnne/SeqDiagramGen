package umlparser;

public class Umlparser {

    public static void main(String[] args) throws Exception {
       if (args[0].equals(("seq"))) {
    	   ParseSeqGen psg = new ParseSeqGen(args[1], args[2], args[3], args[4]);
            psg.start();
        } else {
            System.out.println("Enter 'seq' for the diagram. This is invalid " + args[0]);
        }

    }
}
