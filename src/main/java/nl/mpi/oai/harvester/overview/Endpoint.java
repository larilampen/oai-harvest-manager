/*
 * Copyright (C) 2015, The Max Planck Institute for
 * Psycholinguistics.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License is included in the file
 * LICENSE-gpl-3.0.txt. If that file is missing, see
 * <http://www.gnu.org/licenses/>.
 */

package nl.mpi.oai.harvester.overview;

/**
 * <br> Access to endpoint attributes <br><br>
 *
 * Apart from initialising them to a default value, the following elements fall
 * outside the governance of the harvest cycle. This leaves room for manual
 * intervention. Setting the block attribute to true, for example, will prevent
 * the cycle from harvesting an endpoint that causes trouble. <br><br>
 *
 * kj: get only?
 *
 * <table>
 * <td>
 * URI         <br>
 * group       <br>
 * block       <br>
 * retry       <br>
 * incremental <br>
 * scenario    <br>
 * </td>
 * <td>
 * cycle needs to supply it  <br>
 * cycle needs to supply it  <br>
 * defaults to false         <br>
 * defaults to false         <br>
 * defaults to false         <br>
 * defaults to 'ListRecords' <br>
 * </td>
 * </table><br>
 *
 * kj: initialisation issue, check xsd for obligatory elements
 * A class implementing this interface should initialise the attributes
 * accordingly, and reflect back to the interface. Also explain what is
 * meant by this.
 *
 * The cycle should, after the first attempt of harvesting an endpoint,
 * initialise the following values. After each subsequent attempt, they should
 * update the values. <br><br>
 *
 * kj: maybe point out that setting might be indirect
 *
 * <table>
 * <td>
 * attempted <br>
 * harvested <br>
 * count     <br>
 * increment <br><br>
 * </td>
 * </table>
 *
 * By using the methods defined here, the harvest cycle can track the state of
 * the endpoints.
 *
 * By tracking, the cycle can obtain recent additions to an endpoint, without
 * having to harvest all the records provided by it over and over again. This
 * incremental mode of harvesting is particularly useful when the endpoint
 * provides a large number of records. <br><br>
 *
 * kj: is this still needed?
 * Please refer to the cycle interface for a description of the semantics involved. <br><br>
 *
 * This interface presents an endpoint to the harvest cycle. The harvest cycle
 * can access the ... attributes directly through method parameters and
 * return values. <br><br>
 *
 * Access to ...  is indirect, like for example the date of the most
 * recent harvest. While the interface does provide a method for getting the
 * value of the attribute, the harvest cycle cannot set it by providing a it as
 * a parameter. The doneHarvesting method will determine and set it by itself. <br><br>
 *
 * consider what to do with these paragraphs
 * - list exactly the attributes referenced
 * - also, in each method, list the attributes it deals with
 * - alternatively, combine the two paragraphs, leaving out a reference to the
 *   attributes
 * - maybe leave it out altogether
 *
 * A typical implementation of the Endpoint interface would be an adapter class
 * that reads from and writes to an XML file.
 *
 * @author Kees Jan van de Looij (MPI-PL)
 */
public interface Endpoint {

    /**
     * <br> Get the endpoint URI <br><br>
     *
     * The URI by which the harvest cycle will try to connect to the endpoint.
     *
     * @return endpoint URI
     */
    public abstract String getURI ();

    /**
     * <br> Get the group
     *
     * @return the group the endpoint belongs to
     *
     */
    public abstract String getGroup ();

    /**
     * <br> Find out if an endpoint is blocked <br><br>
     *
     * If blocked, the cycle will not try to harvest the endpoint, regardless
     * of any other specification. <br><br>
     *
     * Note: there is no method for blocking the endpoint. The decision to
     * block an endpoint is not part of the harvesting lifecycle itself. It
     * could be taken, for example, in case the endpoint fails to perform
     * correctly.
     *
     * @return true if the endpoint should be skipped, false otherwise
     */
    public abstract boolean blocked ();

    /**
     * <br> Check if the cycle can retry harvesting the endpoint <br><br>
     *
     * Only if the harvest cycle itself is in retry mode, it can effectively
     * retry harvesting the endpoint. <br><br>
     *
     * Note: like getURI, blocked and allowIncremental harvest, the interface
     * does not provide a method that can set the value of the retry attribute.
     *
     * @return true is a retry is allowed, false otherwise
     */
    public abstract boolean retry();

    /**
     * <br> Check if the cycle can harvest the endpoint incrementally<br><br>
     *
     * In case cycle can harvest the endpoint incrementally, the date of the
     * previous successful harvest determines which records it will be request
     * from the endpoint. <br><br>
     *
     * Note: there is no method for setting the value indicating whether or
     * not incremental harvesting is allowed. The value needs to be specified
     * elsewhere, for example in an XML file managed by a class implementing
     * this interface.
     *
     * @return true if incremental harvesting is allowed, false otherwise
     */
    public abstract boolean allowIncrementalHarvest ();

    /**
     * <br> Get the scenario for harvesting <br><br>
     *
     * A cycle applies a specific scenario for harvesting records. It can,
     * for example, first harvest a list of identifiers to metadata elements,
     * and after that, harvest the records one by one. Alternatively, it can
     * harvest the records directly.
     *
     * Note: there is no method for setting the scenario. The value needs to
     * be specified independently from the harvest cycle.
     *
     * @return the scenario
     */
    public abstract Cycle.Scenario getScenario ();

    /**
     * <br> Return the date for incrementally harvesting <br><br>
     *
     * The date of the most recent and successful harvest cycle. A subsequent
     * cycle will use this date when harvesting the endpoint incrementally. <br><br>
     *
     * Note: a harvesting cycle needs to set the date by invoking the
     * doneHarvesting method.
     *
     * @return the date of most recent successful harvest of the endpoint
     */
    public abstract String getRecentHarvestDate();

    /**
     * <br> Indicate success or failure <br><br>
     *
     * If done equals true, the method sets the date of the most recent and
     * successful harvest to the current date. If false, it will only set the
     * most recent attempt.
     *
     * @param done true in case of success, false otherwise
     */
    public abstract void doneHarvesting(Boolean done);

    /**
     * <br> Get the record count
     *
     * @return the number of records harvested
     */
    public abstract long getCount ();

    /**
     * <br> Set record count
     *
     * @param count the number of records harvested
     */
    public abstract void setCount (long count);

    /**
     * <br> Get the record increment <br><br>
     *
     * The number of records incrementally harvested from the endpoint in
     * the most recent harvest cycle. If the endpoint was not incrementally
     * harvested, the increment will be zero.
     *
     * @return the increment
     */
    public abstract long getIncrement ();

    /**
     * <br> Set the record increment <br><br>
     *
     * The number of records incrementally harvested from the endpoint in the
     * most recent harvest cycle. If the endpoint was not incrementally
     * harvested, the increment will be zero.
     *
     * @param increment the increment
     */
    public abstract void setIncrement (long increment);
}
