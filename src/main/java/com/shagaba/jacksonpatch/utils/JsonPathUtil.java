package com.shagaba.jacksonpatch.utils;

/**
 * Utility class for dealing with JSON paths in the in-memory.
 * 
 * Note: The system has been hardcoded to use the separator "/".
 * 
 * @author Shagaba
 */
public class JsonPathUtil {
    public static final String SEPARATOR = "/";

    /**
     * 
     */
    private static void basicPathChecks(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
        if (!path.equals(path.trim())) {
            throw new IllegalArgumentException(String.format("Path has not been trimmed: '%s'", path));
        }
    }
    
    /**
     * Checks if a path is a root path.
     * 
     * @param path the path to check
     * @return true if the path is a root path, false otherwise
     */
    public static boolean isRoot(String path) {
        basicPathChecks(path);
    	return SEPARATOR.equals(path);
    }

    /**
     * Returns the base name of the path.
     *
     * @param path the path to process
     * @return The base name of the path.
     */
    public static String getBaseName(String path) {
        basicPathChecks(path);
        int sepIndex = path.lastIndexOf(SEPARATOR);
        if (sepIndex != -1 && sepIndex != path.length() -1) {
            return path.substring(sepIndex +1);
        }
        return path;
    }

    /**
     * Returns the parent of the path.
     *
     * @param path the path to process
     * @return The parent path, which may be the empty string ({@code ""}) if
     *      the path is a relative path, or {@code null} if XXXX TODO
     */
    public static String getParent(String path) {
        basicPathChecks(path);
        if (path.equals(SEPARATOR)) {
            return null;
        }
        // Remove the last separator, if it is the last char of the path.
        if (path.length() > 0 && path.endsWith(SEPARATOR)) {
            path = path.substring(0, path.length() -1);
        }
        // Look for the last separator.
        int sepIndex = path.lastIndexOf(SEPARATOR);
        if (sepIndex == 0) {
            return SEPARATOR;
        } else if (sepIndex > 0) {
            return path.substring(0, sepIndex);
        } else {
            return null;
        }
    }

    /**
     * Joins the two paths by inserting the separator character between them.
     *
     * @param parent parent path
     * @param base name of the path
     * @return A merged path.
     */
    public static String join(String parent, String base) {
        basicPathChecks(parent);
        if (parent.endsWith(SEPARATOR)) {
            return parent + getBaseName(base);
        }
        return (parent + SEPARATOR + getBaseName(base));
    }

    /**
     * Tells whether or not this path is absolute.
     * An absolute path starts with the separator "/".
     * @param path the path to process
     * @return true when path is absolute, false otherwise 
     */
    public static boolean isAbsolutePath(String path) {
        basicPathChecks(path);
        return path.startsWith(SEPARATOR);
    }

    /**
     * Tells whether or not this path is non absolute path
     * @param path the path to process
     * @return true when path is relative, false otherwise 
     */
    public static boolean isRelativePath(String path) {
        return !isAbsolutePath(path);
    }

}
