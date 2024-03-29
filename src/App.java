import java.io.*;
import java.util.*;

public class App{
    public static Scanner scan = new Scanner(System.in);

    public long getToday(){
        return (System.currentTimeMillis() / 86400000);
    }

    private void store(Properties props, String propPath) throws Exception{
        props.store(new FileOutputStream(propPath), "Here is your profile!\n" + 
        "You can view the last time you summoned for characters,\n" +
        "and also see the total number of characters you currently have.\n");
    }

    public int summon(Properties props, int choice){
        int left = Integer.parseInt(props.getProperty("summonsLeft"));

        System.out.println("Are you sure you want to use " + choice + " summons?");
        String response = scan.nextLine();
        boolean yes = false;
        while(!yes){
            if(response.toLowerCase().equals("no") || response.toLowerCase().equals("n")){
                return left;
            }
            else if(!(response.toLowerCase().equals("yes") || response.toLowerCase().equals("y"))){
                System.out.println("Please respond with yes or no, or y or n");
                response = scan.nextLine();
            }
            else{
                yes = true;
            }
        }

        if(left == 0){
            System.out.println("You have no more remaining summons for today.\n" +
            "Check in tomorrow for more characters!\n");
        }
        else if(left < 0){
            System.err.println("How the fuck did you get this message?");
            //give them an error character
            /* Ideas for Error Character
             * 
             * Dante with Big Sword
             * Zack with Big Sword
            */
        }
        else{
            left -= choice;
            System.out.println("You summoned " + choice + " heroes!");
        }
        return left;
    }
    public static void main(String[] args) throws Exception{
        if(args.length == 1 && args[0].equals("commands")){
            System.out.println(
            "Welcome to the game! And if you've played before, thanks for supporting us!\n" +
            "Make sure you update your game for new features everyday using javac App.java\n\n" +
            "To play, you'll want to type the words 'java App' followed by the commands you want to use\n\n" +
            "The first command should '-u' followed by your username. After that, select the command you wanted to use.\n\n" +
            "So, if you wanted to summon 10 heroes, you would type\n" +
            "java App -u myusername123 -s 10\n\n" +
            "Here's a list of commands you can use on your journey with the lovely characters of Forever Intertwined!\n\n\n" +
            "-u : username (requires an additional argument)\n" +
            "-s : summon (requires an additional number argument)\n" +
            "-pf : profile page\n" +
            "-h : list of all currently owned heroes\n"
            );

            return;
        }

        if(args.length < 2 || !args[0].equals("-u")){
            System.err.println("Please provide username before using commands\n" +
            "\nExample usage:\n" +
            "java App -u myusername123 -s 10\n" +
            "\nType 'java App commands' for more info\n");

            return;
        }

        if(!new File(args[1] + ".properties").exists()){
            BufferedWriter profileWrite = new BufferedWriter(new FileWriter(args[1] + ".properties"));
            profileWrite.write("#Here is your profile!\n" + //
                    "#You can view the last time you summoned for characters,\n" + //
                    "#and also see the total number of characters you currently have.\n" + //
                    "#\n" + //
                    "#Thu Mar 28 19:56:38 PDT 2024\n" + //
                    "characters=0\n" + //
                    "summonsLeft=10\n" + //
                    "lastSummon=0\n" + //
                    "username=" + args[1] + "\n" + //
                    "");
            profileWrite.close();

            System.out.println("Welcome " + args[1] + "!");
            return;
        }
        else{
            System.out.println("Welcome back " + args[1] + "!");
        }



        App gacha = new App();
        
        String propPath = args[1] + ".properties";           //creates path to properties file and loads properties
        Properties props = new Properties();
        props.load(new FileInputStream(propPath));

        if(args.length > 3 && args[2].equals("-s")){        //checks for commands to the game by player
            props.setProperty("lastSummon", "" + gacha.getToday());
            props.setProperty("summonsLeft", "" + gacha.summon(props, Integer.parseInt(args[3])));
        }
        gacha.store(props, propPath);

        scan.close();
    }
}