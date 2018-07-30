package ir.dimyadi.persiancalendar.view.tasbihat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.tasbihat.CounterActivity;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.view.tasbihat.db.Contract;

public class BaadAlsalahAdapter extends RecyclerView.Adapter<BaadAlsalahAdapter.ViewHolder> {

    private Context context;
    private String[] azkar_list;
    private static int[] num_list;

    //the exact names of columns in dbs
    private static String[] azkarDB = {Contract.ALLAH_AKBAR, Contract.ALHAMDULELLAH, Contract.SOBHAN_ALLAH};

    public BaadAlsalahAdapter(Context context, String[] azkar_list, int[] num_list) {
        this.context = context;
        this.azkar_list = azkar_list;
        BaadAlsalahAdapter.num_list = num_list;
    }

    @Override
    public BaadAlsalahAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.baad_alsalah_item, parent, false), context);
    }

    @Override
    public void onBindViewHolder(final BaadAlsalahAdapter.ViewHolder holder, final int position) {
        final Utils utils = Utils.getInstance(this.context);
        holder.zekr.setText(azkar_list[position]);
        holder.num.setText(utils.formatNumber(String.valueOf(num_list[position])));

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final Utils utils = Utils.getInstance(this.context);

        TextView zekr, num;
        Context context;
        View itemView;
        ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.itemView = itemView;

            zekr = (TextView) itemView.findViewById(R.id.zekr);
            num = (TextView) itemView.findViewById(R.id.num);
            utils.setFont(num);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //if the item's number = 33
            if (num_list[getAdapterPosition()] >= 33) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                builder.setMessage(R.string.hadith)
                        .setPositiveButton("باشه", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

            } else {

                int restSum = 0;
                for (int i = 0; i < num_list.length; i++) {
                    if (i != getAdapterPosition()) {
                        restSum += num_list[i];
                    }
                }
                Intent intent = new Intent(context, CounterActivity.class);
                intent.putExtra("zekr", getAdapterPosition());
                intent.putExtra("num", num_list[getAdapterPosition()]);
                intent.putExtra("sum", restSum);
                intent.putExtra("nameInDB", azkarDB[getAdapterPosition()]);
                context.startActivity(intent);
            }
        }
    }
}
