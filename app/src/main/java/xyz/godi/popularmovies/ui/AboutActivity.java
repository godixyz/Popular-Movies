package xyz.godi.popularmovies.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import xyz.godi.popularmovies.R;

public class AboutActivity extends AppCompatActivity {

    TextView licences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void showLicenceDialog(View view) {
        final Notices notices = new Notices();
        // Picasso
        notices.addNotice(
                new Notice("Square - Picasso", "http://square.github.io/picasso/",
                        "Copyright 2013 Square, Inc.", new ApacheSoftwareLicense20()));
        // Square
        notices.addNotice(
                new Notice("Square - Retrofit", "http://square.github.io/retrofit/",
                        "Copyright 2013 Square, Inc.", new ApacheSoftwareLicense20()));
        // ButterKnife
        notices.addNotice(
                new Notice("ButterKnife", "https://github.com/JakeWharton/butterknife/",
                        "Copyright 2013 Jake Wharton", new ApacheSoftwareLicense20()));

        new LicensesDialog.Builder(AboutActivity.this)
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }
}
