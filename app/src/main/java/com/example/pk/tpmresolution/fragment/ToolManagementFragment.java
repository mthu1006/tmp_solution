package com.example.pk.tpmresolution.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.model.ToolManagementtItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;

public class ToolManagementFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ToolManagementtItem mItem = null;
    private CustomFontTextView txtToolCode,txtStyleName, txtBuyerName, txtJingName, txtJingSeriarNo, txtJingMeterial, txtPart, txtMachineModel, txtJingType,
                    txtCorporation,  txtFactory, txtLine, txtTitleLine, txtStatus, txtJingDate, txtVideoFile, txtPattemFile, txtAttachFile ;
    private CustomFontEditText  txtProcess;
    LinearLayout layoutLine;
    private MaterialLetterIcon imgAvatar;
    MaterialDialog mDialogLoading;
    CustomFontButton mBtn_dialog;

    public ToolManagementFragment(ToolManagementtItem item) {
        this.mItem = item;
    }

    public static ToolManagementFragment newInstance(ToolManagementtItem item) {
        ToolManagementFragment fragment = new ToolManagementFragment(item);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_tool_management, container, false);
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getString(R.string.machine_information));
        ((MainActivity) getActivity()).frag = this;
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());
        ((MainActivity)getActivity()).fab.setVisibility(View.GONE);

        txtToolCode = (CustomFontTextView) root.findViewById(R.id.txt_tool_code);
        txtStyleName = (CustomFontTextView) root.findViewById(R.id.txt_style_name);
        txtBuyerName = (CustomFontTextView) root.findViewById(R.id.txt_buyer_name);
        txtJingName = (CustomFontTextView) root.findViewById(R.id.txt_jing_name);
        txtJingSeriarNo = (CustomFontTextView) root.findViewById(R.id.txt_jing_serial_no);
        txtJingMeterial = (CustomFontTextView) root.findViewById(R.id.txt_jing_meterial);
        txtPart = (CustomFontTextView) root.findViewById(R.id.txt_part);
        txtMachineModel = (CustomFontTextView) root.findViewById(R.id.txt_machine_model);
        txtJingType = (CustomFontTextView) root.findViewById(R.id.txt_jing_type);
        txtCorporation = (CustomFontTextView) root.findViewById(R.id.txt_corporation);
        txtFactory = (CustomFontTextView) root.findViewById(R.id.txt_factory);
        txtLine = (CustomFontTextView) root.findViewById(R.id.txt_line);

        txtTitleLine = (CustomFontTextView) root.findViewById(R.id.txt_title_line);

        txtStatus = (CustomFontTextView) root.findViewById(R.id.txt_status);
        txtVideoFile = (CustomFontTextView) root.findViewById(R.id.txt_video_file);
        txtPattemFile = (CustomFontTextView) root.findViewById(R.id.txt_pattem_file_info);
        txtAttachFile = (CustomFontTextView) root.findViewById(R.id.txt_attach_file);
        txtProcess = (CustomFontEditText) root.findViewById(R.id.txt_process);

        layoutLine = (LinearLayout) root.findViewById(R.id.layout_line);
        imgAvatar = (MaterialLetterIcon) root.findViewById(R.id.img_avatar);

            setupToolManagement();

        return root;
    }

    private void setupToolManagement(){
        if(mItem!=null){
            if(mItem.getAvatar()!=null)
            imgAvatar.setImageBitmap(mItem.getAvatar());
            validationText(txtToolCode, mItem.getTool_code());
            validationText(txtStyleName, mItem.getStyle_name());
            validationText(txtBuyerName, mItem.getBuyer_name());
            validationText(txtJingName, mItem.getJing_name());
            validationText(txtJingSeriarNo, mItem.getJing_serial_no());
            validationText(txtJingMeterial, mItem.getJing_meterial());
            validationText(txtPart,mItem.getPart());
            validationText(txtMachineModel, mItem.getMachine_model());
            validationText(txtJingType, mItem.getJing_type());
            validationText(txtStatus, mItem.getStatus());
            validationText(txtVideoFile, mItem.getVideo_file());
            validationText(txtPattemFile, mItem.getPattem_file());
            validationText(txtAttachFile, mItem.getAttach_file());
            validationText(txtProcess,mItem.getProcess());
            validationText(txtFactory,mItem.getFactory());
            validationText(txtCorporation,mItem.getCorporation());



            if(Validation.checkNullOrEmpty(mItem.getLine())){
                if(!Validation.checkNullOrEmpty(mItem.getWarehouse())) {
                    txtTitleLine.setText("Warehouse");
                    txtLine.setText(mItem.getWarehouse());
                }else layoutLine.setVisibility(View.GONE);
            }else {
                txtTitleLine.setText("Line");
                txtLine.setText(mItem.getLine());
            }

        }
    }

    public void validationText(TextView tv, String s){
        if(!Validation.checkNullOrEmpty(s))
            tv.setText(s);
        else tv.setText(AppConstants.DEFAULT_NULL_TEXT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /*void ShowDialogError(String message) {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_error);
        CustomFontTextView txt1 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content1);
        CustomFontTextView txt2 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        txt1.setText("Change status failed");
        txt2.setText(message);
        mDialog.show();
    }

*/

}
