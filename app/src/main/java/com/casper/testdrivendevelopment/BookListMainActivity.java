package com.casper.testdrivendevelopment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {
    public static final int ITEM_ID = 1;
    public static final int CONTEXT_MUNE_ADD = 2;
    public static final int CONTEXT_MUNE_ABOUT = 3;
    private static final int REQUEST_CODE_NEW_BOOK = 901;
    private static final int CONTEXT_MUNE_CHANGE = 4;
    private static final int REQUEST_CODE_CHANGE_BOOK = 902;
    private List<Book> ListBooks=new ArrayList<>();

    BookAdapter bookAdapter;
    Booksaver booksaver;
    private AdapterView.AdapterContextMenuInfo menuInfo;
    private int position;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        booksaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        booksaver=new Booksaver(this);
        ListBooks=booksaver.load();
        if(ListBooks.size()==0)
            init();

        bookAdapter = new BookAdapter(
                BookListMainActivity.this, R.layout.list_view_item_book, ListBooks);
        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<>();
        datas.add(new BookListFragment(bookAdapter));
        datas.add(new WebViewFragment());
        datas.add(new MapViewFragment());
        datas.add(new GameFragment());
        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        titles.add("游戏");
        myPageAdapter.setTitles(titles);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
// 将适配器设置进ViewPager
        viewPager.setAdapter(myPageAdapter);
// 将ViewPager与TabLayout相关联
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == findViewById(R.id.list_view_books)) {
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(ListBooks.get(info.position).getTitle());
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, ITEM_ID, 0, "删除");
            menu.add(0, CONTEXT_MUNE_ADD, 0, "添加");
            menu.add(0, CONTEXT_MUNE_CHANGE, 0, "修改");
            menu.add(0, CONTEXT_MUNE_ABOUT, 0, "关于");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW_BOOK:
            if (resultCode == RESULT_OK) { String title = data.getStringExtra("title");
        int insertPosition = data.getIntExtra("insert_position", 0);
        double price = data.getDoubleExtra("price", 0);
        getListBooks().add(insertPosition, new Book(title, R.drawable.book_no_name));
        bookAdapter.notifyDataSetChanged();
            }
        break;
            case REQUEST_CODE_CHANGE_BOOK:
                if (resultCode == RESULT_OK) {

                    int insertPosition = data.getIntExtra("insert_position", 0);
                    Book bookatposition=getListBooks().get(insertPosition);
                    bookatposition.setTitle(data.getStringExtra("title"));
                    bookAdapter.notifyDataSetChanged();
                }
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_ID :
                final AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ListBooks.remove(itemPosition);

                                bookAdapter.notifyDataSetChanged();
                                Toast.makeText(BookListMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
                break;
            case CONTEXT_MUNE_ADD:
                Intent intent = new Intent(this, EditBookMainActivity.class);
                intent.putExtra("title", "无名书籍");intent.putExtra("price", 1);
                intent.putExtra("insert_position", ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_NEW_BOOK);
                /*ListBooks.add(new Book("(无名书籍）", R.drawable.book_no_name));
                bookAdapter.notifyDataSetChanged();*/
                break;
            case CONTEXT_MUNE_CHANGE:
                Intent intent2 = new Intent(this, EditBookMainActivity.class);

                intent2.putExtra("title",ListBooks.get(position).getTitle());
                intent2.putExtra("insert_position", ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                startActivityForResult(intent2, REQUEST_CODE_CHANGE_BOOK);
                break;
            case CONTEXT_MUNE_ABOUT:
                Toast.makeText(BookListMainActivity.this,"图书列表 v1.0 code by lfj",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);

    }

    private void init() {
        ListBooks.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        ListBooks.add(new Book("创新工程实践", R.drawable.book_no_name));
        ListBooks.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
    }

    public List<Book> getListBooks() {
        return ListBooks;
    }

    public class BookAdapter extends ArrayAdapter<Book> {

        private int resourceId;

        public BookAdapter(Context context, int resource, List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Book book = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_book_cover)).setImageResource(book.getCoverResourceId());
            ((TextView) view.findViewById(R.id.text_view_book_title)).setText(book.getTitle());
            return view;
        }

    }
}
