package com.example.CLI.Parser.Rules;

import com.example.CLI.Commands.Operation;
import com.example.CLI.Commands.WeakQuoting;
import com.example.CLI.Environment.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Правило для нераспознанных литералов.
 */
public class LiteralRule implements Rule {

    private static final String regex = "\\s*[^\\s]+\\s*";
    @NotNull static private Pattern word;
    @Nullable private String literal;
    @NotNull private Context context;

    static {
        word = Pattern.compile("[^\\s]+");
    }

    public LiteralRule(@NotNull Context context) {
        this.context = context;
    }

    @Override @NotNull
    public Type getType() {
        return Type.WORD;
    }

    @Override @NotNull
    public Integer getLevel() {
        return 0;
    }

    @Override
    public boolean isMatching(@NotNull String input) {
        return input.matches(regex);
    }

    @NotNull
    @Override
    public ArrayList<String> split(@NotNull String input) {
        if (!input.matches(regex)) {
            throw new IllegalStateException("Input does not match with rule.");
        }

        var matcher = word.matcher(input);
        matcher.find();
        literal = input.substring(matcher.start(), matcher.end());

        return new ArrayList<>();
    }

    @NotNull
    @Override
    public Operation createOperation(ArrayList<Operation> args) {
        if (literal == null) {
            throw new IllegalStateException("Can't create Operation.");
        }

        return new WeakQuoting(context, literal);
    }
}
