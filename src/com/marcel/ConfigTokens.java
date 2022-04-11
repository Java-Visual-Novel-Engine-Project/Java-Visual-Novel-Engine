package com.marcel;

import java.util.Dictionary;
import java.util.List;

public class ConfigTokens {

    public static class ConfigObject {}

    public static class ConfigLabelObject extends ConfigObject {
        public String label;
        public ConfigObject[] objects;

        public ConfigLabelObject(String label, ConfigObject[] objects) {
            this.label = label;
            this.objects = objects;
        }
    }

    public static class ConfigVariableObject extends ConfigObject {
        public String name;
        public ConfigVariableObjectType variable;

        public ConfigVariableObject(String name, ConfigVariableObjectType variable) {
            this.name = name;
            this.variable = variable;
        }
    }

    public static class ConfigVariableObjectType {
        @Override
        public String toString() {
            if (this instanceof ConfigVariableString)
                return  "\"" + ((ConfigVariableString) this).value + "\"";
            else if (this instanceof ConfigVariableInteger)
                return ((ConfigVariableInteger) this).value + "";
            else if (this instanceof ConfigVariableDouble)
                return ((ConfigVariableDouble) this).value + "";
            else if (this instanceof ConfigVariableBoolean)
                return ((ConfigVariableBoolean) this).value ? "true" : "false";
            else if (this instanceof ConfigVariableArray)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[");

                for (int i = 0; i < ((ConfigVariableArray) this).values.length; i++) {
                    sb.append(((ConfigVariableArray) this).values[i]);
                    if (i != ((ConfigVariableArray) this).values.length - 1)
                    sb.append(", ");
                }

                sb.append("]");
                return sb.toString();
            }
            else if (this instanceof ConfigVariableDictionary)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("{");

                ConfigVariableDictionary dict = ((ConfigVariableDictionary) this);
                for (int i = 0; i < dict.values.size(); i++) {
                    sb.append(dict.values.get(i).name);
                    sb.append(" = ");
                    sb.append(dict.values.get(i).variable);
                    if (i != (dict.values.size() - 1))
                        sb.append(", ");
                }

                sb.append("}");
                return sb.toString();
            }
            else if (this instanceof ConfigVariableReference)
            {
                return "$" + ((ConfigVariableReference) this).variableName;
            }


            return "<IDK>";
        }

    }

    public static class ConfigVariableReference extends ConfigVariableObjectType {
        public String variableName;

        public ConfigVariableReference(String variableName) {
            this.variableName = variableName;
        }
    }

    public static class ConfigVariableInteger extends ConfigVariableObjectType {
        public int value;

        public ConfigVariableInteger(int value) {
            this.value = value;
        }
    }

    public static class ConfigVariableDouble extends ConfigVariableObjectType {
        public double value;

        public ConfigVariableDouble(double value) {
            this.value = value;
        }
    }

    public static class ConfigVariableString extends ConfigVariableObjectType {
        public String value;

        public ConfigVariableString(String value) {
            this.value = value;
        }
    }

    public static class ConfigVariableBoolean extends ConfigVariableObjectType {
        public boolean value;

        public ConfigVariableBoolean(boolean value) {
            this.value = value;
        }
    }

    public static class ConfigVariableArray extends ConfigVariableObjectType {
        public ConfigVariableObjectType[] values;

        public ConfigVariableArray(ConfigVariableObjectType[] values) {
            this.values = values;
        }
    }

    public static class ConfigVariableDictionary extends ConfigVariableObjectType {
        public List<ConfigVariableObject> values;

        public ConfigVariableDictionary(List<ConfigVariableObject> values) {
            this.values = values;
        }
    }
}
