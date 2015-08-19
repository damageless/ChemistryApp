package edu.weber.chemistryapp.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.weber.chemistryapp.ChargeView;
import edu.weber.chemistryapp.ElementView;
import edu.weber.chemistryapp.ElementLinkView;
import edu.weber.chemistryapp.R;
import edu.weber.chemistryapp.loader.Parser;
import edu.weber.chemistryapp.loader.Serializer;
import edu.weber.chemistryapp.models.animations.Animation;
import edu.weber.chemistryapp.models.animations.MoveCharge;
import edu.weber.chemistryapp.models.animations.TapMultipleElements;
import edu.weber.chemistryapp.models.animations.TapStructure;
import edu.weber.chemistryapp.models.Transition;
import edu.weber.chemistryapp.models.elements.Charge;
import edu.weber.chemistryapp.models.elements.Element;
import edu.weber.chemistryapp.models.elements.Link;
import edu.weber.chemistryapp.persistence.Preferences;

/**
 * Created by agessel on 6/6/15.
 */
public class CreateAnimationsFragment extends Fragment {
    private List<ElementView> mElementViews = new LinkedList<>();
    private List<ElementLinkView> mElementLinkViews = new LinkedList<>();

    private boolean mIsLinking = false;
    private boolean mIsLinkingStructure = false;
    private boolean mIsAddingCharge = false;

    private ElementView mFirstLink = null;

    private Snackbar mSnackbar;
    private Animation mAnimation;

    private TextView mDescriptionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        final View layout = inflater.inflate(R.layout.fragment_create_animations, container, false);

        final FloatingActionsMenu menu = (FloatingActionsMenu) layout.findViewById(R.id.multiple_actions);

        final FloatingActionButton addElementButton = (FloatingActionButton) layout.findViewById(R.id.add_element_button);
        addElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLinking) {
                    return;
                }

                new MaterialDialog.Builder(getActivity())
                        .title(R.string.element_symbol)
                        .content(R.string.element_symbol_input)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.element_symbol_hint, R.string.create_element_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                Element element = new Element(input.toString(), true);
                                ElementView elementView = new ElementView(getActivity(), element);
                                elementView.addListener(mElementListener);
                                mElementViews.add(elementView);
                                ((ViewGroup) layout).addView(elementView);
                                menu.toggle();
                            }
                        }).show();
            }
        });

        final FloatingActionButton linkElementsButton = (FloatingActionButton) layout.findViewById(R.id.link_elements_button);
        linkElementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsLinking = true;
                preventAllElementsFromMoving();

                mSnackbar = Snackbar.make(getView(), R.string.instructor_linking_text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelLinking();
                            }
                        });

                mSnackbar.show();
                menu.toggle();
            }
        });

        final FloatingActionButton linkStructuresButton = (FloatingActionButton) layout.findViewById(R.id.link_structures_button);
        linkStructuresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsLinkingStructure = true;
                preventAllElementsFromMoving();

                mSnackbar = Snackbar.make(getView(), R.string.instructor_linking_structures_text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelStructureLinking();
                            }
                        });

                mSnackbar.show();
                menu.toggle();
            }
        });

        final FloatingActionButton addChargeButton = (FloatingActionButton) layout.findViewById(R.id.add_charge_button);
        addChargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsAddingCharge = true;
                preventAllElementsFromMoving();

                mSnackbar = Snackbar.make(getView(), R.string.instructor_adding_charges_text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelAddingCharges();
                            }
                        });

                mSnackbar.show();
                menu.toggle();
            }
        });

        final FloatingActionButton addDescriptionButton = (FloatingActionButton) layout.findViewById(R.id.add_description);
        addDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.animation_description)
                        .content(R.string.animation_description_text)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.animation_description_hint, R.string.animation_description_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                mAnimation.mDescription = input.toString();
                                showDescription();
                                menu.toggle();
                            }
                        }).show();
            }
        });

        new MaterialDialog.Builder(getActivity())
                .title(R.string.animation_type)
                .items(R.array.animation_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int position, CharSequence charSequence) {
                        switch (position) {
                            case 0:
                                mAnimation = new TapMultipleElements("TapMultipleElements", null, 2);
                                break;
                            case 1:
                                mAnimation = new TapStructure("TapStructure", "Some description");
                                linkStructuresButton.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                mAnimation = new MoveCharge("MoveCharge", "Some description");
                                linkStructuresButton.setVisibility(View.GONE);
                            default:
                                mAnimation = new TapStructure("TapStructure", "Some description");
                                break;
                        }
                        return true;
                    }

                })
                .positiveText(R.string.create)
                .show();


        return layout;
    }

    private void showDescription() {
        if (mDescriptionTextView == null) {
            mDescriptionTextView = new TextView(getActivity());
            mDescriptionTextView.setText(mAnimation.mDescription);
            mDescriptionTextView.setX(100);
            mDescriptionTextView.setY(75);
            mDescriptionTextView.setTextColor(getResources().getColor(android.R.color.black));
            mDescriptionTextView.setTextSize(18);
            mDescriptionTextView.setSingleLine(false);
            mDescriptionTextView.setLines(5);
            mDescriptionTextView.setMaxWidth(900);
            ((ViewGroup) getView()).addView(mDescriptionTextView);
        } else {
            mDescriptionTextView.setText(mAnimation.mDescription);
        }
    }

    private void preventAllElementsFromMoving() {
        for (final ElementView elementView : mElementViews) {
            elementView.getElement().mCanMove = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create_animations, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_save:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.transition_name)
                        .content(R.string.input_content)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.input_hint, R.string.create_animation_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                Serializer serializer = new Serializer();
                                Preferences preferences = Preferences.createInstance(getActivity());

                                mAnimation.mTransitions.add(new Transition(input.toString(), "ToTransition"));
                                mAnimation.mElements = getElements();
                                mAnimation.mLinks = getLinks();

                                Parser parser = new Parser();
                                List<Animation> animations = parser.parseAnimations(preferences.getAnimations());
                                animations.add(0, mAnimation);
                                preferences.setAnimations(serializer.serialize(animations));

                                clearAll();
                                Toast.makeText(getActivity(), getString(R.string.animation_created_successfully), Toast.LENGTH_LONG).show();

                            }
                        }).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void clearAll() {
        mFirstLink = null;

        mElementViews.clear();
        mElementLinkViews.clear();
        ((ViewGroup) getView()).removeAllViews();
    }

    private List<Link> getLinks() {
        List<Link> links = new ArrayList<>();

        for (ElementLinkView linkView : mElementLinkViews) {
            links.add(new Link(linkView.link1.getElement().mElementId, linkView.link2.getElement().mElementId));
        }

        return links;
    }

    private List<Element> getElements() {
        List<Element> elements = new LinkedList<>();
        for (ElementView elementView : mElementViews) {
            elements.add(elementView.getElement());
        }

        return elements;
    }

    public List<ElementView> getLinkedElements(List<ElementView> linkedElements, ElementView elementView) {
        List<ElementView> elements = new ArrayList<>();
        int size = linkedElements.size();

        for (ElementLinkView link : mElementLinkViews) {
            if (link.link1.equals(elementView) && !linkedElements.contains(link.link2)) {
                elements.add(link.link1);
                elements.add(link.link2);

                linkedElements.add(link.link2);
            }

            if (link.link2.equals(elementView) && !linkedElements.contains(link.link1)) {
                elements.add(link.link1);
                elements.add(link.link2);

                linkedElements.add(link.link1);
            }
        }

        if (linkedElements.size() != size) {
            for (ElementView link : linkedElements) {
                elements.addAll(getLinkedElements(linkedElements, link));
            }
        }

        return elements;
    }

    private ElementView.Listener mElementListener = new ElementView.Listener() {
        @Override
        public void onAtomMoved(float x, float y) {
        }

        @Override
        public void onAtomClicked(final ElementView clickedElementView) {
            if (mIsLinking) {
                handleLinking(clickedElementView);
            } else if (mIsLinkingStructure) {
                handleLinkingStructure(clickedElementView);
            } else if (mIsAddingCharge) {
                handleAddCharge(clickedElementView);
            }
        }

        private void handleLinkingStructure(final ElementView clickedElementView) {
            if (mFirstLink != null) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.transition_name)
                        .content(R.string.enter_transition_for_linking)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.input_hint, R.string.create_animation_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                setLinkedElementsUUID(clickedElementView, UUID.randomUUID());
                                setLinkedElementsUUID(mFirstLink, UUID.randomUUID());
                                mAnimation.mTransitions.add(createStructureTransitionBetweenElements(input.toString(), clickedElementView, mFirstLink));
                                selectLinkedElements(mFirstLink, false);
                                mSnackbar.dismiss();
                                mIsLinkingStructure = false;

                            }
                        }).show();
            } else {
                mFirstLink = clickedElementView;
                selectLinkedElements(clickedElementView, true);
            }
        }

        private void selectLinkedElements(ElementView elementView, boolean selected) {
            List<ElementView> links = getLinkedElements(new CopyOnWriteArrayList<ElementView>(), elementView);
            for (ElementView linked : links) {
                linked.setSelected(selected);
            }
        }

        private void setLinkedElementsUUID(ElementView elementView, UUID uuid) {
            List<ElementView> links = getLinkedElements(new CopyOnWriteArrayList<ElementView>(), elementView);
            for (ElementView linked : links) {
                linked.getElement().mGroupId = uuid;
            }
        }

        private void handleLinking(final ElementView clickedElementView) {
            if (mFirstLink != null) {
                if (shouldCreateTransition()) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.transition_name)
                            .content(R.string.enter_transition_for_linking)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(R.string.input_hint, R.string.create_animation_prefill, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    createLink(createTransitionBetweenElements(input.toString(), clickedElementView, mFirstLink), clickedElementView);
                                }
                            }).show();
                } else {
                    createLink(null, clickedElementView);
                }

            } else {
                mFirstLink = clickedElementView;
            }
        }
    };

    private void handleAddCharge(ElementView elementView) {
        elementView.mHasCharge = true;
        elementView.getElement().mCharge = new Charge();
        ChargeView chargeView = new ChargeView(getActivity());
        chargeView.setLinkedElementView(elementView);

        mIsAddingCharge = false;

        ((ViewGroup) getView()).addView(chargeView);
        preventAllElementsFromMoving();
        cancelAddingCharges();
    }

    private void createLink(Transition transitionBetweenElements, ElementView clickedElementView) {
        ElementLinkView link = new ElementLinkView(getActivity(), clickedElementView, mFirstLink);
        mElementLinkViews.add(link);

//        mFirstLink.addNewLink(clickedElement, link);
        mFirstLink.getElement().mCanMove = true;
//        mElementViews.remove(mFirstLink);
        ((ViewGroup) getView()).addView(link);
        if (shouldCreateTransition()) {
            mAnimation.mTransitions.add(transitionBetweenElements);
        }
        cancelLinking();
    }

    private boolean shouldCreateTransition() {
        return mAnimation instanceof TapMultipleElements;
    }

    private Transition createTransitionBetweenElements(String name, ElementView elementView1, ElementView elementView2) {
        Transition transition = new Transition("SomeName", name);

        LinkedList<String> elementIds = new LinkedList<>();
        elementIds.add(elementView1.getElement().mElementId.toString());
        elementIds.add(elementView2.getElement().mElementId.toString());
        transition.mElements = elementIds;

        return transition;
    }

    private Transition createStructureTransitionBetweenElements(String name, ElementView elementView1, ElementView elementView2) {
        Transition transition = new Transition("SomeName", name);

        LinkedList<String> elementIds = new LinkedList<>();
        elementIds.add(elementView1.getElement().mGroupId.toString());
        elementIds.add(elementView2.getElement().mGroupId.toString());
        transition.mElements = elementIds;

        return transition;
    }

    private void cancelLinking() {
        mFirstLink = null;
        for (ElementView elementView : mElementViews) {
            elementView.getElement().mCanMove = true;
        }
        mIsLinking = false;
        mSnackbar.dismiss();
    }

    private void cancelStructureLinking() {
        for (ElementView elementView : mElementViews) {
            elementView.getElement().mCanMove = true;
        }
        mIsLinkingStructure = false;
        mSnackbar.dismiss();
    }

    private void cancelAddingCharges() {
        for (ElementView elementView : mElementViews) {
            elementView.getElement().mCanMove = true;
        }
        mIsAddingCharge = false;
        mSnackbar.dismiss();
    }
}
