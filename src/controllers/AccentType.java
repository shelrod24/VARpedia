package controllers;

public enum AccentType {

    EnglishUS("-ven-us", "English US"), EnglishUK("", "English UK"), Spanish("-veo", "Spanish"),  German("-vde", "German");

    String _flag;
    String _name;

    AccentType(String flag, String name){
        _flag = flag;
        _name = name;
    }


    public String ReturnFlag(){
        return _flag;
    }

    @Override
    public String toString(){
        return _name;
    }

}
