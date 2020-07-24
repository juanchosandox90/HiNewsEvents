package com.huawei.hinewsevents.ui.home

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.Utils
import java.util.*
import kotlin.math.roundToLong


class HomeFragmentListDemo : Fragment() {

    val TAG: String = "HomeFragmentList"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_home_list, container, false)

        setAdapterNewsList(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            Log.d(  TAG, "onViewCreated getInt(ARG_OBJECT).toString() ${getInt(ARG_OBJECT)}")
            setAdapterNewsList(view)
        }
    }

    // To Test view
    private fun setAdapterNewsList(view: View) {
        val randomNumb = (3..10).random()
        Log.d(TAG, "setAdapterNewsList with randomNumb $randomNumb")
        val viewAdapter = MyAdapter(Array(randomNumb) { "NewsEvents ${it + 1}" })
        view.findViewById<RecyclerView>(R.id.recyclerview_list).run {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    companion object {
        private const val ARG_OBJECT = "object"
    }


    open class HomeFragmentListDemoCollectionAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            val fragment = HomeFragmentListDemo()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }
    }


    ///
    // To Test view
    class MyAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            // create a new view
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

            return ViewHolder(itemView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            holder.item.findViewById<TextView>(R.id.item_date_time).text = listOfDateTime[position % listOfDateTime.size].toString()

            holder.item.findViewById<TextView>(R.id.item_title).text = listOfTitles[position % listOfTitles.size].toString()

            holder.item.findViewById<TextView>(R.id.item_detail).text = listOfContents[position % listOfContents.size].toString()

            //holder.item.findViewById<ImageView>(R.id.item_image).setImageResource(listOfPictures[position % listOfPictures.size])

            var imageUri: String = Uri.parse(
                                "android.resource://" +  holder.item.context.packageName + "/" +
                                        listOfPictures[position % listOfPictures.size].toString()).toString()


            Utils.loadAndSetImageWithGlide(
                holder.item.context,
                holder.item.findViewById<ImageView>(R.id.item_image),
                imageUri
            )

            var randomRatingD: Double =  Utils.getRandomDouble(10,1)
            var randomRating: String = ( randomRatingD.toString() ).substring(0,3)

            holder.item.findViewById<TextView>(R.id.item_rating).text = randomRating.toString()

            holder.item.findViewById<TextView>(R.id.item_rating).setTextColor(
                holder.item.context.resources.getColor( Utils.getColorRatingLevel( randomRating.toFloat() ) ) )

            holder.item.setOnClickListener {

                Log.d("Adapter", "rating   :$randomRating")
                Log.d("Adapter", "dateTime :${listOfDateTime[position]}")
                Log.d("Adapter", "title    :${listOfTitles[position]}")
                Log.d("Adapter", "contents :${listOfContents[position]}")
                Log.d("Adapter", "imageUri :${imageUri.toString()}")

                Log.d("HomeFragmentAdapter","onBindViewHolder item onCLick and item.findNavController().currentDestination ${holder.item.findNavController().currentDestination} " +
                        " ${holder.item.findNavController().currentDestination?.id} - navigation_home ${R.id.navigation_home}" )
                // TODO set and edit bundle content
                val bundle = bundleOf("rating" to randomRating,
                                    "dateTime" to listOfDateTime[position] ,
                                    "title" to listOfTitles[position] ,
                                    "contents" to listOfContents[position] ,
                                    "imageUri" to imageUri,
                                    "rating" to randomRating
                )

                Navigation.findNavController(holder.itemView).navigate(R.id.action_navigation_home_to_homeDetailFragment, bundle )
                //holder.item.findNavController().navigate( R.id.action_navigation_home_to_homeDetailFragment )

            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size

        companion object {
            const val NEWS_CONTENT = "news"
        }

        private val listOfPictures = listOf(
            R.drawable.news_1_raster,
            R.drawable.news_2_raster,
            R.drawable.news_3_raster,
            R.drawable.news_4_raster,
            R.drawable.news_5_raster,
            R.drawable.news_6_raster,
            R.drawable.news_7_raster,
            R.drawable.news_8_raster
        )

        private val listOfDateTime = listOf(
            "2020-07-21 11:10",
            "2020-07-22 12:20",
            "2020-07-23 13:30",
            "2020-07-24 14:40",
            "2020-07-25 15:50",
            "2020-07-26 16:00",
            "2020-07-27 17:10",
            "2020-07-28 18:20"
        )
        private val listOfTitles = listOf(
            "Huawei\\'nin En Güçlü Dizüstü Bilgisayarı MateBook X Pro, Türkiye\\'de Satışta",
            "Huawei, HarmonyOS'la İlgili 3 Marka Başvurusunda Bulundu",
            "Huawei'nin Telefon Sektörünün Kralı Olmaya Devam Edeceğini Gösteren Rapor",
            "Huawei, Mate 40 ve Mate 40 Pro Ekranlarını BOE'den Alacak",
            "Huawei, EMUI 11, 2020 3. Çeyrekte Mate 40 ile Birlikte Tanıtılacak",
            "Huawei Türkiye: HMS'nin Geliştirilmesinde Türk Mühendislerin Önemi Büyük",
            "Huawei'nin Petal Search Uygulamasıyla Nasıl Uygulama İndirilir?",
            "Huawei, 10 Bin Dolar Ödüllü Fotoğrafçılık Yarışmasını Başlattı (Siz de Katılabilirsiniz)"
        )

        private val listOfContents = listOf(
            "Huawei, 10. Nesil Intel Core i7 işlemci, 3K Ultra FullView ekran gibi özellikleriyle öne çıkan MateBook X Pro\\'yu Türkiye\\'de satışa sundu. \n" +
                    "10. nesil Intel Core i7-10510U ve i5-10210U işlemcilerden güç alan Huawei MateBook X Pro, 2GB GDDR5 belleğe sahip NVIDIA GeForce MX250 ile oyun ve grafik düzenleme konularında beklentileri karşılıyor. \n" +
                    "16 GB LPDDR3 RAM’e sahip olan cihaz, 56Wh kapasiteli bataryasıyla 13 saate kadar video oynatma, 15 saate kadar ofis uygulamaları kullanmayı mümkün kılıyor.\n" +
                    "Şu anda MateBook X Pro’nun yalnızca 10. nesil Intel Core i7-10510U işlemcili versiyonunun 14.999 TL fiyat etiketiyle satışa sunulmuş durumda",

            "Huawei, Çin'de 3 yeni ticari marka başvurusu yaptı. Bu marka başvurularının tümü de HarmonyOS ile ilgili olarak karşımıza çıkıyor. Huawei'nin Çin'de yaptığı başvurular; Huawei HarmonyOS, HarmonyOS Connected ve HarmonyOS Linked isimlerini kapsıyor.\n" +
                    "Çinli teknoloji üreticisi Huawei, bir süredir kendi ekosistemini oluşturmanın peşinde. Şirket, ABD yönetiminin uyguladığı ticaret yasağının ardından kendi işletim sistemini geliştirdiğini açıkladı. \n" +
                    "Süreç içerisinde hem çok fazla hem de kafa karıştıran açıklamalarda bulunan şirket, geçtiğimiz yıl Huawei Geliştirici Konferansı kapsamında da yeni mobil işletim sistemi HarmonyOS'u resmen duyurdu.\n",

            "Nikkei Asia tarafından hazırlanan bir rapor, Huawei'nin haziran ayında da dünyanın en büyük akıllı telefon üreticisi olacağını gösteriyor. Nisan ayı itibarıyla zirveye oturan ve pozisyonunu korumayı başaran Huawei, tahtı Samsung'a kolay kolay bırakmayacak gibi görünüyor.\n" +
                    "Akıllı telefon sektörü, çok büyük bir rekabetin içerisinde. Teknoloji üreticileri, piyasaya sürdüğü akıllı telefonlarla tüketicileri mest etmeye çalışırken geliştirdikleri yeni teknoloji ve özelliklerle de en iyinin kendileri olduğunu göstermeye çalışıyorlar.\n" +
                    "Uzunca bir süredir Samsung, sektörün bir numaralı ismi olarak karşımızdayken geçtiğimiz nisan ayında Çinli Huawei, Samsung'un tahtını elinden almayı başardı. Görünüşe göre Samsung, en azından haziran ayında da bir numara olamayacak.",

            "Çinli akıllı telefon üreticisi Huawei, Mate 40 ve Mate 40 Pro modellerinin ekran tedarikini Çin merkezli BOE şirketinden yapacak. \n" +
                    "Mate 40'ta sadece BOE ekranları kullanılırken Pro varyantında BOE'ye ek olarak Samsung ve LG ekranlar da kullanılacak.\n" +
                    "Çin merkezli ekran üreticisi BOE'nin 'on-cell' OLED ekran teknolojisi pazarın liderleri konumunda bulunan Samsung Display ve LG Display'i yakalamış görünüyor. \n" +
                    "Şirket, Huawei Mate 40 ve Mate 40 Pro'nun ekranlarının tedarikini üstlenecek.\n" +
                    "BOE, Huawei Mate 40 modelinin ekranlarının tek tedarikçisi olacakken Huawei Mate 40 Pro'da ekranlar üç şirketten alınacak: \n" +
                    "BOE, Samsung Display ve LG Display. BOE, bu senin başında Huawei P40 modelinin ekranları için de teklif götürmüş ancak o dönem yeterli görülmemişti.",

            "Huawei Tüketici Yazılımları Bölümü Başkanı Wang Chenglu, yeni Android arayüzü sürümlerinin yılın üçüncü çeyreğinde tanıtılacağını ve Mate 40 ile birlikte geleceğini açıkladı.\n" +
                    "Bir röportaj sırasında son derece önemli açıklamalarda bulunan Huawei Tüketici Yazılımları Bölümü Başkanı Wang Chenglu, EMUI 11 ile ne zaman tanışacağımızı açıkladı. \n" +
                    "EMUI ve Huawei Club’ın 8. yıldönümü için geçtiğimiz günlerde bir etkinlik düzenleyen Chenglu, önce EMUI’nin en büyük kilometre taşlarından bahsettiği, sonra da yeni sürüm ile ne zaman tanışacağımızı bildirdi.",

            "Huawei, Google hizmetlerini kullanamaması nedeniyle kendi mobil ekosistemi Huawei Mobil Servisleri'ne yoğunlaştı. \n" +
                    "Huawei Türkiye Ar-Ge Merkezi Direktörü Hüseyin Hai, Türk mühendislerin Huawei Mobil Servisleri'nin gelişimine önemli katkı sağladığını belirtti.\n" +
                    "\n" +
                    "ABD'nin Huawei'yi ticari kara listeye alması nedeniyle Çinli teknoloji devi, yeni akıllı telefonlarında Google hizmetlerini kullanamıyor. \n" +
                    "Huawei, bu noktada Huawei Mobil Servisleri (HMS) ile kullanıcılara kendi ekosistemini yayınlayarak bir alternatif sunmaya çalışıyor.\n" +
                    " Huawei Türkiye Ar-Ge Merkezi Direktörü Hüseyin Hai'ye göre Türkiye'deki geliştirme üssü, Türkiye'de ve dünyada HMS ekosisteminin gelişmesi açısında büyük bir öneme sahip.\n" +
                    "\n" +
                    "Huawei Türkiye Ar-Ge Merkezi Direktörü Hüseyin Hai'nin açıklamalarına göre HMS, kullanıcılara mobil hizmet ve içerikler sunarken, yazılım geliştiricilere de farklı imkanlar sağlayan bir ekosistem sunuyor. " +
                    "Huawei Türkiye Ar-Ge Merkezi, özel olarak oluşturduğu HMS ekibi ve 150'den fazla mühendisiyle mobil hizmetlerin gelişiminde önemli rol oynuyor.",

            "Çinli teknoloji devi Huawei, ABD hükümeti ile yaşadığı sorunların ardından kendi mobil ekosistemini kurabilmek için harekete geçti. \n" +
                    "Bu bağlamda her ne kadar akıllı telefonlarda kullanılmayacak olsa da kendi mobil işletim sistemini duyuran Huawei, kendi uygulama mağazasına verdiği önemi artırmanın yanı sıra Huawei'ye özel bazı uygulamalar da geliştirdi. Huawei'nin piyasaya sürdüğü son uygulamalardan bir tanesi de Petal Search.\n" +
                    "Petal Search, temelde bir internet tarayıcısı. Ancak ABD ile arası bozulduktan sonra akıllı telefonlarında Google Play Store kullanamayan Huawei, bu internet tarayıcısını bir uygulama dağıtım merkezi haline de getirdi. Öyle ki Honor ve Huawei markalı akıllı telefon kullanıcıları, Petal Search'ü kullanarak hem App Gallery hem de harici kaynaklardaki mobil uygulamalara erişebiliyorlar. ",

            "Huawei, geçtiğimiz yıl 520 bin kişinin katılımıyla büyük bir rekora imza attığı \"Next Image\" yarışmasının bu yılki başvurularını da başlattığını açıkladı. Şirketin \"Next Image 2020\" isimli fotoğrafçılık yarışması, 5 farklı kategoride gerçekleşecek ve \"Yılın Fotoğrafçısı\"na Huawei P40 Pro ile birlikte 10 bin dolar ödül verilecek.\n" +
                    "Çinli teknoloji şirketi Huawei, sadece teknolojik ürünleriyle değil düzenlediği etkinliklerle de ön plana çıkmayı başarıyor. Zaman zaman küçük çaplı etkinliklerle tüketicilerin ilgisini çekmeyi başaran şirket, şimdiyse büyük ödüller dağıtacağı yeni bir yarışma başlattığını açıkladı."
        )

    }


    override fun onAttach(context: Context) {
        Log.d(TAG, "1 onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "2 onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "4 onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "5 onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "6 onResume")
        super.onResume()
    }

    override fun onStop() {
        Log.d(TAG, "7 onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "8 onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "9 onDetach")
        super.onDetach()
    }


}