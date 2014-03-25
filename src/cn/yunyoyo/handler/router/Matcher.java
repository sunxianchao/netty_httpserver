package cn.yunyoyo.handler.router;


public interface Matcher {
    public boolean match(String uri);
}

class StartsWithMatcher implements Matcher {
    private String route;

    private StartsWithMatcher(String route) {
        this.route = route;
    }

    public boolean match(String uri) {
        return uri.startsWith(route);
    }
}

class EndsWithMatcher implements Matcher {
    private String route;

    private EndsWithMatcher(String route) {
        this.route = route;
    }

    public boolean match(String uri) {
        return uri.endsWith(route);
    }
}

class EqualsMatcher implements Matcher {
    private String route;

    private EqualsMatcher(String route) {
        this.route = route;
    }

    public boolean match(String uri) {
        return uri.equals(route);
    }
}