package com.monke.monkeybook.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monke.monkeybook.R;
import com.monke.monkeybook.help.BookshelfHelp;
import com.monke.monkeybook.help.FileHelp;
import com.monke.monkeybook.utils.FileStack;
import com.monke.monkeybook.utils.FileUtil;
import com.monke.monkeybook.view.activity.ImportBookActivity;
import com.monke.monkeybook.view.adapter.FileSystemAdapter;
import com.monke.monkeybook.widget.itemdecoration.DividerItemDecoration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FileCategoryFragment extends BaseFileFragment {
    @BindView(R.id.file_category_rv_content)
    RecyclerView mRvContent;
    TextView mTvPath;
    Unbinder unbinder;

    private FileStack mFileStack;
    private String rootFilePath;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_file_category, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImportBookActivity) {
            mTvPath = ((ImportBookActivity) context).mTvPath;
        }
    }

    @Override
    protected void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        mFileStack = new FileStack();
        setUpAdapter();
    }

    private void setUpAdapter() {
        mAdapter = new FileSystemAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext())));
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mAdapter.setOnItemClickListener((view, pos) -> {
                    File file = mAdapter.getItem(pos);
                    if (file.isDirectory()) {
                        //保存当前信息。
                        FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                        snapshot.filePath = mTvPath.getText().toString();
                        snapshot.files = new ArrayList<>(mAdapter.getItems());
                        snapshot.scrollOffset = mRvContent.computeVerticalScrollOffset();
                        mFileStack.push(snapshot);
                        //切换下一个文件
                        toggleFileTree(file);
                    } else {
                        //如果是已加载的文件，则点击事件无效。
                        String id = mAdapter.getItem(pos).getAbsolutePath();
                        if (BookshelfHelp.isInBookShelf(id)) {
                            return;
                        }
                        //点击选中
                        mAdapter.setCheckedItem(pos);
                        //反馈
                        if (mListener != null) {
                            mListener.onItemCheckedChange(mAdapter.getItemIsChecked(pos));
                        }
                    }
                }
        );
    }

    @Override
    protected void firstRequest() {
        super.firstRequest();
        upRootFile(Environment.getExternalStorageDirectory().getPath());
    }

    public void showSdSelector() {
        if (getContext() != null) {
            List<String> list = FileUtil.getStorageData(getContext());
            if (list != null) {
                String[] filePathS = list.toArray(new String[list.size()]);
                int select = list.indexOf(rootFilePath);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("选择SD卡")
                        .setSingleChoiceItems(filePathS, select < 0 ? 0 : select, (dialogInterface, i) -> {
                            upRootFile(filePathS[i]);
                            dialogInterface.dismiss();
                        })
                        .create();
                dialog.show();
            }
        }
    }

    public boolean backLastCategory() {
        FileStack.FileSnapshot snapshot = mFileStack.pop();
        if (snapshot == null) return false;
        int oldScrollOffset = mRvContent.computeHorizontalScrollOffset();
        mTvPath.setText(snapshot.filePath);
        mAdapter.refreshItems(snapshot.files);
        mRvContent.scrollBy(0, snapshot.scrollOffset - oldScrollOffset);
        //反馈
        if (mListener != null) {
            mListener.onCategoryChanged();
        }
        return true;
    }

    private void upRootFile(String rootFilePath) {
        this.rootFilePath = rootFilePath;
        toggleFileTree(new File(rootFilePath));
    }

    private void toggleFileTree(File file) {
        //路径名
        mTvPath.setText(file.getPath().replace(rootFilePath, ""));
        //获取数据
        File[] files = file.listFiles(new SimpleFileFilter());
        //转换成List
        List<File> rootFiles = Arrays.asList(files);
        //排序
        Collections.sort(rootFiles, new FileComparator());
        //加入
        mAdapter.refreshItems(rootFiles);
        //反馈
        if (mListener != null) {
            mListener.onCategoryChanged();
        }
    }

    @Override
    public int getFileCount() {
        int count = 0;
        Set<Map.Entry<File, Boolean>> entrys = mAdapter.getCheckMap().entrySet();
        for (Map.Entry<File, Boolean> entry : entrys) {
            if (!entry.getKey().isDirectory()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o2.isDirectory() && o1.isFile()) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public class SimpleFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(".")) {
                return false;
            }
            //文件夹内部数量为0
            if (pathname.isDirectory() && pathname.list().length == 0) {
                return false;
            }

            //文件内容为空,或者不以txt为开头
            return pathname.isDirectory() ||
                    (pathname.length() != 0 && pathname.getName().endsWith(FileHelp.SUFFIX_TXT));
        }
    }
}
