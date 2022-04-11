package com.marcel;

public class ConfigErrors {
    public static class TestError extends Exception
    {
        public TestError(String message)
        {
            super(message);
        }
    }

    public static class UnterminatedStringException extends Exception
    {
        public UnterminatedStringException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class InvalidNumberException extends Exception
    {
        public InvalidNumberException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class InvalidBooleanException extends Exception
    {
        public InvalidBooleanException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class UnterminatedCommentException extends Exception
    {
        public UnterminatedCommentException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class UnterminatedLabelHeadException extends Exception
    {
        public UnterminatedLabelHeadException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class UnterminatedLabelBodyException extends Exception
    {
        public UnterminatedLabelBodyException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class UnterminatedArrayException extends Exception
    {
        public UnterminatedArrayException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class UnterminatedDictionaryException extends Exception
    {
        public UnterminatedDictionaryException(String message, String context)
        {
            super(message + context);
        }
    }

    public static class MissingValueException extends Exception
    {
        public MissingValueException(String message, String context)
        {
            super(message + context);
        }
    }

}
