package com.example.nutritionproject.Custom.java.Utility;

public class EventContext
{
    private String message;
    private String error;
    private Object data;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getError() {return error;}
    public void setError(String error) {
        this.error = error;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }


    private EventContext(Builder builder)
    {
        this.message = builder.message;
        this.error = builder.error;
        this.data = builder.data;
    }

    public static class Builder
    {
        private String message;
        private String error;
        private Object data;

        public Builder()
        {

        }

        public Builder withMessage(String message)
        {
            this.message = message;

            return this;
        }

        public Builder withError(String error)
        {
            this.error = error;

            return this;
        }

        public Builder withData(Object data)
        {
            this.data = data;

            return this;
        }

        public EventContext build() {
            return new EventContext(this);
        }
    }

}
