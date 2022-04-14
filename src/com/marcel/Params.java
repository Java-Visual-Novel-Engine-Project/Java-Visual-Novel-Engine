package com.marcel;

/**
 * Provides utility methods for creating and retrieving parameters.
 * For detailed instructions see {@link Param}
 *
 * @see Param
 */
public class Params {
    /**
     * Create a named parameter
     *
     * @param name  the name of the parameter (can be an enum)
     * @param value the value of the parameter
     * @param <V>   the type of the value
     * @return a new named parameter
     */
    public static <V> Param<V> param(Object name, V value) {
        return new Param(name, value);
    }

    /**
     * Retrieve a named parameter.
     *
     * @param params the array containing the parameters
     * @param name   the name of the parameter to retrieve
     * @param def    a default value which will be retruned if the parameter is not in the array
     * @param <V>    the type of the value of the param
     * @return the parameter value specified in params or the default value
     */
    public static <V> V getParam(Param[] params, Object name, V def) {
        for (Param a : params)
            if (a.name.equals(name)) return (V) a.value;
        return def;
    }
}
