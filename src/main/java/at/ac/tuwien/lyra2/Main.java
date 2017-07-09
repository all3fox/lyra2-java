package at.ac.tuwien.lyra2;

import picocli.CommandLine;

public class Main {
    public static void main(String[] argv) {
        ConsoleArgs args = CommandLine.populateCommand(new ConsoleArgs(), argv);

        if (args.help) {
            CommandLine.usage(new ConsoleArgs(), System.err);

            return;
        }

        LyraParams params = new LyraParams(
                args.klen, args.t_cost, args.m_cost,
                args.ROUNDS, args.N_COLS, args.BLOCK_LEN_INT64
        );

        byte[] hash = new byte[args.klen];
        byte[] pass = args.pass.getBytes();
        byte[] salt = args.salt.getBytes();

        // TODO: you should overwrite the params.pass now

        Lyra2.phs(hash, pass, salt, params);

        System.out.println("Output:");
        echo.bytes(hash, hash.length);
    }
}
