package com.dhikaadeputra.creditsimulator.controller.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandRegistry {
    private Map<String, Command> commands = new HashMap<>();

    public void register(Command command){
        commands.put(command.name(), command);
    }

    public Optional<Command> find(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public List<Command> all() {
        return new ArrayList<>(commands.values());
    }
}