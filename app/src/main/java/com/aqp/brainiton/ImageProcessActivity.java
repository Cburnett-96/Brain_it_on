package com.aqp.brainiton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aqp.brainiton.ml.Model;
import com.aqp.brainiton.other.SoundPoolManager;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

public class ImageProcessActivity extends AppCompatActivity {

    TextView result, confidence, textView;
    ImageView imageView;
    Button picture, btnBack;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_purple));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_purple));

        textView = findViewById(R.id.textView);
        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        btnBack = findViewById(R.id.detail_back_button);

        // Launch camera if we have permission
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        } else {
            //Request camera permission if we don't have it.
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            finish();
        }

        picture.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        });

        btnBack.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            finish();
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Bench", "Telly", "Desk", "Telephone", "Lamp", "Penny", "Hurricane", "PictureFrame", "Kettle", "Tongs", "Purse", "Seesaw", "Screw", "Compass"
                    ,"Balloon","Banana","Bee","Bicycle","Blender","Blower","Bridge","Brocolli","Bulb","Butterfly","Carpet","Cat","Charcoal","Comb","Dice","Drums","Elephant","Frog","Grapes","Grass","Horse","Icecream","Joey","Lion","Moth"
                    ,"Mug","Newspaper","Onion","Pail","Parrot","Perfume","Pig","Rabbit","Rainbow","Rooster","Scissors","Sponge","Squash","Turtle"};
            result.setText(classes[maxPos]);

            String mBench = "A long seat for more than one person.";
            if(result.getText().toString().equals("Bench")){
                textView.setText(mBench);
            }
            String mTelly = "An electronic device that receives television signals and displays them on a screen.";
            if(result.getText().toString().equals("Telly")){
                textView.setText(mTelly);
            }
            String mDesk = "A piece of furniture with a writing surface and usually drawers or other compartments.";
            if(result.getText().toString().equals("Desk")){
                textView.setText(mDesk);
            }
            String mTelephone = "A device for speaking to someone in another place by means of electrical signals.";
            if(result.getText().toString().equals("Telephone")){
                textView.setText(mTelephone);
            }
            String mLamp = "Any of various devices furnishing artificial light, as by electricity or gas";
            if(result.getText().toString().equals("Lamp")){
                textView.setText(mLamp);
            }
            String mHurricane = "A severe tropical cyclone usually with heavy rains and winds";
            if(result.getText().toString().equals("Hurricane")){
                textView.setText(mHurricane);
            }
            String mPenny = "A penny is a coin or a unit of currency in various countries";
            if(result.getText().toString().equals("Penny")){
                textView.setText(mPenny);
            }
            String mPictureFrame = "A representation of a person or scene in the form of a print or transparent slide";
            if(result.getText().toString().equals("PictureFrame")){
                textView.setText(mPictureFrame);
            }
            String mKettle = "A metal pot for stewing or boiling; usually has a lid.";
            if(result.getText().toString().equals("Kettle")){
                textView.setText(mKettle);
            }
            String mTongs = "Any of various devices for taking hold of objects; usually have two hinged legs with handles above and pointed hooks below.";
            if(result.getText().toString().equals("Tongs")){
                textView.setText(mTongs);
            }
            String mSeesaw = "A plaything consisting of a board balanced on a supporter ; the board is ridden up and down by children at either end.";
            if(result.getText().toString().equals("Seesaw")){
                textView.setText(mSeesaw);
            }
            String mScrew = "Tighten or fasten by means of screwing motions.";
            if(result.getText().toString().equals("Screw")){
                textView.setText(mScrew);
            }
            String mCompass = "Instrument for finding directions.";
            if(result.getText().toString().equals("Compass")){
                textView.setText(mCompass);
            }
            String mMug  = "A large cup for hot drinks.";
            if(result.getText().toString().equals("Mug")){
                textView.setText(mMug );
            }
            String mPig = "A farm animal with short legs and a curved tail, kept for its meat.";
            if(result.getText().toString().equals("Pig")){
                textView.setText(mPig);
            }
            String mCat  = "A small animal with fur, four legs, a tail, and claws usually kept as a pet.";
            if(result.getText().toString().equals("Cat")){
                textView.setText(mCat);
            }
            String mBee = "An insect that makes honey.";
            if(result.getText().toString().equals("Bee")){
                textView.setText(mBee);
            }
            String mJoey = "A baby kangaroo.";
            if(result.getText().toString().equals("Joey")){
                textView.setText(mJoey);
            }
            String mLion = "A large wild member of the cat family";
            if(result.getText().toString().equals("Lion")){
                textView.setText(mLion);
            }
            String mDice = "A small cube that has between one and six spots or numbers on its sides.";
            if(result.getText().toString().equals("Dice")){
                textView.setText(mDice);
            }
            String mDrum = "A musical instrument that you play by hitting it with a stick or your hand.";
            if(result.getText().toString().equals("Drum")){
                textView.setText(mDrum);
            }
            String mFrog = "A small amphibian with long back legs that allow it to hop.";
            if(result.getText().toString().equals("Frog")){
                textView.setText(mFrog);
            }
            String mPail = "A open container with a handle to carry water.";
            if(result.getText().toString().equals("Pail")){
                textView.setText(mPail);
            }
            String mComb = "A plastic or metal width of a row of teeth.";
            if(result.getText().toString().equals("Comb")){
                textView.setText(mComb);
            }
            String mMoth = "An insect with wings that flies at night.";
            if(result.getText().toString().equals("Moth")){
                textView.setText(mMoth);
            }
            String mBulb = "A device used to create lights.";
            if(result.getText().toString().equals("Bulb")){
                textView.setText(mBulb);
            }
            String mGrass = "A very common plant with thin green leaves that covers the ground";
            if(result.getText().toString().equals("Grass")){
                textView.setText(mGrass);
            }
            String mOnion = "A round vegetable that grows underground that has a strong, sharp smell and taste.";
            if(result.getText().toString().equals("Onion")){
                textView.setText(mOnion);
            }
            String mGrape = "A small, sweet fruit fermented to produce wine.";
            if(result.getText().toString().equals("Grapes")){
                textView.setText(mGrape);
            }
            String mSponge = "A sponge has holes and absorbs liquid.";
            if(result.getText().toString().equals("Sponge")){
                textView.setText(mSponge);
            }
            String mParrot = "A bright color tropical bird trained to imitate human speech.";
            if(result.getText().toString().equals("Parrot")){
                textView.setText(mParrot);
            }
            String mBridge = "A structure that is built over a river or road.";
            if(result.getText().toString().equals("Bridge")){
                textView.setText(mBridge);
            }
            String mCarpet = "A heavy fabric used as floor cover.";
            if(result.getText().toString().equals("Carpet")){
                textView.setText(mCarpet);
            }
            String mBlower = "A device that blows cool or hot air.";
            if(result.getText().toString().equals("Blower")){
                textView.setText(mBlower);
            }
            String mTurtle = "A reptile that has a shell covering its body.";
            if(result.getText().toString().equals("Turtle")){
                textView.setText(mTurtle);
            }
            String mRooster = "A male chicken.";
            if(result.getText().toString().equals("Rooster")){
                textView.setText(mRooster);
            }
            String mPerfume = "A liquid that gives people a scent.";
            if(result.getText().toString().equals("Perfume")){
                textView.setText(mPerfume);
            }
            String mRainbow = "A multi-color arc appears when rains.";
            if(result.getText().toString().equals("Rainbow")){
                textView.setText(mRainbow);
            }
            String mSquash = "A large round fruit with many seeds.";
            if(result.getText().toString().equals("Squash")){
                textView.setText(mSquash);
            }
            String mBalloon = "Used as toys or decoration.";
            if(result.getText().toString().equals("Balloon")){
                textView.setText(mBalloon);
            }
            String mBicycle = "A two-wheeled vehicle that's propelled by foot pedals and steered with handlebars.";
            if(result.getText().toString().equals("Bicycle")){
                textView.setText(mBicycle);
            }
            String mBroccoli = "A vegetable with a green stem with a flower bud at the top.";
            if(result.getText().toString().equals("Broccoli")){
                textView.setText(mBroccoli);
            }
            String mElephant = "A four-footed animal with big ears and a long trunk. ";
            if(result.getText().toString().equals("Elephant")){
                textView.setText(mElephant);
            }
            String mScissor = "A cutting tool used in cloth, paper, etc.";
            if(result.getText().toString().equals("Scissor")){
                textView.setText(mScissor);
            }
            String mIcecream = "A frozen dessert made from cream, milk, sugar, etc.";
            if(result.getText().toString().equals("Icecream")){
                textView.setText(mIcecream);
            }
            String mCharcoal = "A black substance left over when the wood is heated in the absence of oxygen.";
            if(result.getText().toString().equals("Charcoal")){
                textView.setText(mCharcoal);
            }
            String mButterfly = "A flying insect that has colorful wings.";
            if(result.getText().toString().equals("Butterfly")){
                textView.setText(mButterfly);
            }
            String mNewspaper = "A paper that contains information about news, events, etc.";
            if(result.getText().toString().equals("Newspaper")){
                textView.setText(mNewspaper);
            }
            String mPurse = "A container used for carrying money and small personal items or accessories";
            if(result.getText().toString().equals("Purse")){
                textView.setText(mPurse);
            }
            String mBanana = "A curved yellow fruit with thick skin and soft sweet flesh.";
            if(result.getText().toString().equals("Banana")){
                textView.setText(mBanana);
            }

            /*String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidence.setText(s);*/

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) Objects.requireNonNull(data).getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

    }
}