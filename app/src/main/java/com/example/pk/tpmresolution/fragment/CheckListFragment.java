package com.example.pk.tpmresolution.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckListFragment extends Fragment  implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    public static ViewPager mViewPager;
    private Typeface typeface;
    public static CustomFontTextView txtDateMonthYear;
    Toolbar mToolbar;
    DatePickerDialog datePickerDialog;
    DailyCheckListFragment daily;
    public static Calendar calendar;

    public CheckListFragment() {
        // Required empty public constructor
    }

    public static CheckListFragment newInstance() {
        CheckListFragment fragment = new CheckListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_check_list, container, false);

        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getTextArray(R.array.navigation_array_tile)[3]);
        mViewPager = (ViewPager)root.findViewById(R.id.view_pager);
        mToolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(getString(R.string.checklist_tab_title1));
        ButterKnife.bind(this, root);
        daily = DailyCheckListFragment.newInstance();
        setupViewPager(mViewPager);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        txtDateMonthYear = (CustomFontTextView) root.findViewById(R.id.txt_date_month_year);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Log.d("Kien", "Curent date "+sdf.format(calendar.getTime()));
        txtDateMonthYear.setText(sdf.format(calendar.getTime()));
        txtDateMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        return root;
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(daily, getString(R.string.checklist_daily));
        //adapter.addFragment(WeeklyCheckListFragment.newInstance(), getString(R.string.checklist_tab_title2));
        viewPager.setAdapter(adapter);

        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Light.ttf");
        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                    }
                    //mToolbar.setTitle( getString(R.string.checklist_daily));
                }else{
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                    }
                    adapter.getItem(position).onResume();
                   // mToolbar.setTitle( getString(R.string.checklist_weekly));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        txtDateMonthYear.setText(sdf.format(calendar.getTime()));
        daily.choosedDate = calendar.getTime();
        daily.SetSelectedDate(calendar);
    }

    public static  void setDate(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        txtDateMonthYear.setText(sdf.format(d));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
