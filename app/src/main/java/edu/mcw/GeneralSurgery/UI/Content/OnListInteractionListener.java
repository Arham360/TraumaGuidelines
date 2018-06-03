package edu.mcw.GeneralSurgery.UI.Content;

import edu.mcw.GeneralSurgery.models.Option;

/**
 * Created by arham on 3/9/18.
 */

interface OnListInteractionListener {

    void onRestartFragmentInteraction();

    void onListFragmentInteraction(Option option);

    void onPrevFragmentInteraction();

    void onListFeedbackFragmentInteraction(Option option);
}
