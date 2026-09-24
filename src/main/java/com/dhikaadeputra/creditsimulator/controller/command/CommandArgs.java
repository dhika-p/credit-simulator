package com.dhikaadeputra.creditsimulator.controller.command;

final class CommandArgs {

    private CommandArgs() {
    }

    // Controller memecah input per spasi, jadi "nama dengan spasi" disatukan lagi di sini.
    static String text(String[] args) {
        return stripQuotes(String.join(" ", args).trim());
    }

    static String stripQuotes(String text) {
        if (text.length() >= 2 && (text.startsWith("\"") && text.endsWith("\"")
                || text.startsWith("'") && text.endsWith("'"))) {
            return text.substring(1, text.length() - 1).trim();
        }
        return text;
    }
}
