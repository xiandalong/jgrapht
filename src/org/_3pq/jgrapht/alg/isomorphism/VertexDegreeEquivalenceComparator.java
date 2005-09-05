/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2005, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------
 * VertexDegreeEquivalenceComparator.java
 * -----------------
 * (C) Copyright 2005, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.alg.isomorphism;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.util.equivalence.EquivalenceComparator;

/**
 *  two vertexes are equivalent if and only if :
 *  <ol><li>they have the same IN degree<p> 
 *  AND
 *  <li>they have the same OUT degree
 * 	</ol>
 *	@author Assaf
 *	@since	Jul 21, 2005
 * 
 */
public class VertexDegreeEquivalenceComparator implements EquivalenceComparator {

	/** */
	public VertexDegreeEquivalenceComparator()
	{
	
	}
	
	/** 
	 * compares the in degrees and the out degrees of the two vertexes.
	 * <p> One may reside in an Undirected Graph and the other in a Directed graph , or both
	 * on the same graph type.
	 * @see org._3pq.jgrapht.util.equivalence.EquivalenceComparator#equivalenceCompare(java.lang.Object, java.lang.Object, Object, Object)
	 */
	public boolean equivalenceCompare(Object vertex1, Object vertex2, Object context1, Object context2) {
		//note that VertexDegreeComparator cannot be used. It supports only directed graphs.
		InOutDegrees inOut1 = getInOutDegrees((Graph)context1 , vertex1);
		InOutDegrees inOut2 = getInOutDegrees((Graph)context2 , vertex2);
		boolean result = inOut1.equals(inOut2);
		return result;
	}

	/** 
	 * hashs using the in & out degree of a vertex
	 * @see org._3pq.jgrapht.util.equivalence.EquivalenceComparator#equivalenceHashcode(java.lang.Object, Object)
	 */ 
	public int equivalenceHashcode(Object vertex, Object context) {
		
		
		InOutDegrees inOut = getInOutDegrees((Graph)context , vertex);
		
		
		//hash it using the string hash. use the format N '-' N
		StringBuffer sb=new StringBuffer();
		sb.append(String.valueOf(inOut.inDegree));
		sb.append("-"); //to diffrentiate inner and outer
		sb.append(String.valueOf(inOut.outDegree));
		return sb.toString().hashCode();
	}
	
	/**
	 * calculate the In and Out degrees of vertexes.
	 * supported graph types: UnDirectedGraph , DirectedGraph
	 * In UnDirected graph , the in = out (as if it was undirected and
	 * every edge is both in the out edge)
	 * @param contextGraph
	 * @param vertex
	 * @return
	 */
	protected InOutDegrees getInOutDegrees(Graph aContextGraph , Object vertex)
	{
		int inVertexDegree = 0;
		int outVertexDegree = 0;
		int uniqueId=-1;
		if ( aContextGraph instanceof UndirectedGraph)
		{
			UndirectedGraph undirectedGraph = (UndirectedGraph) aContextGraph;
			inVertexDegree = undirectedGraph.degreeOf(vertex);
			outVertexDegree = inVertexDegree; //it is UNdirected
		}
		else if ( aContextGraph instanceof DirectedGraph)
		{
			DirectedGraph directedGraph = (DirectedGraph) aContextGraph;
			inVertexDegree = directedGraph.inDegreeOf(vertex);
			outVertexDegree =directedGraph.outDegreeOf(vertex);
		}
		else
		{
			throw new RuntimeException("contextGraph is of unsupported type . It must be one of these two :"
					+" UndirectedGraph or DirectedGraph");
		}
		return new InOutDegrees(inVertexDegree,outVertexDegree);
		
	} 
	/** simple structure used to hold the two ints: vertex in degree 
	 * and vertex out degree.
	 * Usefull as returned value for method which calculate both in the same time.
	 *	@author Assaf
	 *	@since	Jul 21, 2005
	 */
	protected class InOutDegrees
	{
		public int inDegree;
		public int outDegree;
		
		public InOutDegrees(int aInDegree,int aOutDegree) {
				this.inDegree = aInDegree;
				this.outDegree= aOutDegree;
		}
		/** check both inDegree and outDegree.
		 * does not check class type to save time. If should be
		 * used with caution.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			InOutDegrees other = (InOutDegrees)obj;
			return 	(	(this.inDegree == other.inDegree)
					&&  (this.outDegree == other.outDegree) );
			
		}
		
	}


}