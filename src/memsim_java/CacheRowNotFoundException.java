/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memsim_java;

/**
 *
 * @author zalatnaicsongor
 */
public class CacheRowNotFoundException extends Exception {
    public CacheRowNotFoundException(String Message) {
        super(Message);
        Main.stats.addCacheFault(); //inkrementáljuk a cachefault változót
    }
}