package packagepractice.facade;


/**
 * [EnumerationIterator.java]
 */


import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Sang Hyup Lee
 * @version 1.0
 *
 */
public class EnumerationIterator implements Iterator {

    Enumeration enumeration;
    
    public EnumerationIterator(Enumeration enumeration) {
        this.enumeration = enumeration;
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        // TODO Auto-generated method stub
        return this.enumeration.hasMoreElements();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next() {
        // TODO Auto-generated method stub
        return this.enumeration.nextElement();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

}