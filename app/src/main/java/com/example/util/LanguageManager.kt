package com.example.util

import java.util.Locale

data class SupportedLanguage(
    val code: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String,
    val ttsLocale: Locale
)

object LanguageManager {
    val languages = listOf(
        SupportedLanguage("en", "English", "English", "🌐", Locale.ENGLISH),
        SupportedLanguage("hi", "Hindi", "हिंदी", "🇮🇳", Locale("hi", "IN")),
        SupportedLanguage("kn", "Kannada", "ಕನ್ನಡ", "🌾", Locale("kn", "IN")),
        SupportedLanguage("te", "Telugu", "తెలుగు", "🌱", Locale("te", "IN")),
        SupportedLanguage("ta", "Tamil", "தமிழ்", "🌿", Locale("ta", "IN")),
        SupportedLanguage("ml", "Malayalam", "മലയാളം", "🥥", Locale("ml", "IN")),
        SupportedLanguage("mr", "Marathi", "मराठी", "🪴", Locale("mr", "IN")),
        SupportedLanguage("bn", "Bengali", "বাংলা", "🍃", Locale("bn", "IN"))
    )

    private val translations = mapOf(
        // General UI Strings
        "app_title" to mapOf(
            "en" to "AgriVoice AI",
            "hi" to "एग्रीवॉयस एआई",
            "kn" to "ಅಗ್ರಿ ವಾಯ್ಸ್ ಎಐ",
            "te" to "అగ్రివాయిస్ AI",
            "ta" to "அக்ரிவாய்ஸ் AI",
            "ml" to "അഗ്രിവോയ്സ് AI",
            "mr" to "अॅग्रीव्हॉइस AI",
            "bn" to "অ্যাগ্রিভয়েস এআই"
        ),
        "talk_to_agrivoice" to mapOf(
            "en" to "TALK TO AGRIVOICE",
            "hi" to "एग्रीवॉयस से बात करें",
            "kn" to "ಅಗ್ರಿ ವಾಯ್ಸ್ ಜೊತೆ ಮಾತನಾಡಿ",
            "te" to "అగ్రివాయిస్‌తో మాట్లాడండి",
            "ta" to "அக்ரிவாய்ஸிடம் பேசுங்கள்",
            "ml" to "അഗ്രിവോയ്സിനോട് സംസാരിക്കുക",
            "mr" to "अॅग्रीव्हॉइसशी बोला",
            "bn" to "অ্যাগ্রিভয়েসের সাথে কথা বলুন"
        ),
        "ask_anything_subtitle" to mapOf(
            "en" to "Tap microphone & ask anything about your farm in your language",
            "hi" to "माइक दबाएं और अपनी भाषा में खेती के बारे में कुछ भी पूछें",
            "kn" to "ಮೈಕ್ ಒತ್ತಿ ಮತ್ತು ನಿಮ್ಮ ಭಾಷೆಯಲ್ಲಿ ನಿಮ್ಮ ಕೃಷಿ ಬಗ್ಗೆ ಏನನ್ನಾದರೂ ಕೇಳಿ",
            "te" to "మైక్ నొక్కండి మరియు మీ భాషలో మీ పొలం గురించి ఏదైనా అడగండి",
            "ta" to "மைக் அழுத்தி உங்கள் மொழியில் பண்ணை பற்றி கேளுங்கள்",
            "ml" to "മൈക്ക് അമർത്തി നിങ്ങളുടെ ഭാഷയിൽ കൃഷിയെക്കുറിച്ച് ചോദിക്കുക",
            "mr" to "माईक दाबा आणि आपल्या भाषेत शेतीबद्दल काहीही विचारा",
            "bn" to "মাইক টিপুন এবং আপনার ভাষায় আপনার খামার সম্পর্কে জিজ্ঞাসা করুন"
        ),
        "greeting_morning" to mapOf(
            "en" to "Good Morning",
            "hi" to "शुभ प्रभात",
            "kn" to "ಶುಭೋದಯ",
            "te" to "శుభోదయం",
            "ta" to "காலை வணக்கம்",
            "ml" to "സുപ്രഭാതം",
            "mr" to "शुभ सकाळ",
            "bn" to "সুপ্রভাত"
        ),
        "today_briefing" to mapOf(
            "en" to "Today's Farm Briefing",
            "hi" to "आज की खेत रिपोर्ट",
            "kn" to "ಇಂದಿನ ಕೃಷಿ ಸಂಕ್ಷಿಪ್ತ ವರದಿ",
            "te" to "ఈ రోజు వ్యవసాయ బ్రీఫింగ్",
            "ta" to "இன்றைய பண்ணை சுருக்கம்",
            "ml" to "ഇന്നത്തെ കാർഷിക വിവരണം",
            "mr" to "आजचे शेती मार्गदर्शन",
            "bn" to "আজকের খামারের সারসংক্ষেপ"
        ),
        "farm_health" to mapOf(
            "en" to "Farm Health Score",
            "hi" to "खेत का स्वास्थ्य स्कोर",
            "kn" to "ಕೃಷಿ ಆರೋಗ್ಯ ಸ್ಕೋರ್",
            "te" to "పొలం ఆరోగ్య స్కోరు",
            "ta" to "பண்ணை ஆரோக்கிய மதிப்பெண்",
            "ml" to "ഫാം ആരോഗ്യ സ്കോർ",
            "mr" to "शेती आरोग्य गुण",
            "bn" to "খামার স্বাস্থ্য স্কোর"
        ),
        "healthy" to mapOf(
            "en" to "Healthy",
            "hi" to "स्वस्थ (उत्तम)",
            "kn" to "ಆರೋಗ್ಯಕರ (ಉತ್ತಮ)",
            "te" to "ఆరోగ్యంగా ఉంది",
            "ta" to "ஆரோக்கியமானது",
            "ml" to "ആരോഗ്യം",
            "mr" to "उत्तम स्थिती",
            "bn" to "সুস্থ"
        ),
        "watch" to mapOf(
            "en" to "Watch",
            "hi" to "निगरानी रखें",
            "kn" to "ಗಮನಿಸಿ",
            "te" to "పరిశీలించండి",
            "ta" to "கவனிக்கவும்",
            "ml" to "ശ്രദ്ധിക്കുക",
            "mr" to "लक्ष ठेवा",
            "bn" to "নজর রাখুন"
        ),
        "attention" to mapOf(
            "en" to "Attention Needed",
            "hi" to "ध्यान देने की आवश्यकता",
            "kn" to "ಹೆಚ್ಚಿನ ಗಮನ ಅಗತ್ಯ",
            "te" to "శ్రద్ధ అవసరం",
            "ta" to "கவனம் தேவை",
            "ml" to "ശ്രദ്ധ വേണം",
            "mr" to "काळजी आवश्यक",
            "bn" to "মনোযোগ প্রয়োজন"
        ),
        "weather" to mapOf(
            "en" to "Weather Intelligence",
            "hi" to "मौसम पूर्वानुमान",
            "kn" to "ಹವಾಮಾನ ಮಾಹಿತಿ",
            "te" to "వాతావరణ సమాచారం",
            "ta" to "வானிலை தகவல்",
            "ml" to "കാലാവസ്ഥ വിവരങ്ങൾ",
            "mr" to "हवामान अंदाज",
            "bn" to "আবহাওয়ার পূর্বাভাস"
        ),
        "irrigation" to mapOf(
            "en" to "Smart Irrigation",
            "hi" to "स्मार्ट सिंचाई",
            "kn" to "ಸ್ಮಾರ್ಟ್ ನೀರಾವರಿ",
            "te" to "స్మార్ట్ సాగునీరు",
            "ta" to "நீர்ப்பாசன ஆலோசனை",
            "ml" to "നനയ്ക്കൽ ഉപദേശം",
            "mr" to "स्मार्ट पाणी व्यवस्थापन",
            "bn" to "স্মার্ট সেচ"
        ),
        "market" to mapOf(
            "en" to "Mandi Prices & Trends",
            "hi" to "मंडी भाव और रुझान",
            "kn" to "ಮಾರುಕಟ್ಟೆ ಬೆಲೆ ಮತ್ತು ಟ್ರೆಂಡ್",
            "te" to "మార్కెట్ ధరలు",
            "ta" to "சந்தை விலைகள்",
            "ml" to "വിപണി വിലകൾ",
            "mr" to "बाजार भाव आणि कल",
            "bn" to "বাজার দর ও প্রবণতা"
        ),
        "plant_scanner" to mapOf(
            "en" to "Crop Health Scanner",
            "hi" to "फसल रोग स्कैनर",
            "kn" to "ಬೆಳೆ ರೋಗ ಸ್ಕ್ಯಾನರ್",
            "te" to "పంట వ్యాధి స్కానర్",
            "ta" to "பயிர் நோய் ஸ்கேனர்",
            "ml" to "വിള രോഗ സ്കാനർ",
            "mr" to "पीक रोग स्कॅनर",
            "bn" to "ফসল রোগ স্ক্যানার"
        ),
        "listen_briefing" to mapOf(
            "en" to "Listen to Morning Briefing",
            "hi" to "सुबह की रिपोर्ट सुनें",
            "kn" to "ಬೆಳಗಿನ ವರದಿ ಆಲಿಸಿ",
            "te" to "ఉదయపు నివేదిక వినండి",
            "ta" to "காலை சுருக்கத்தைக் கேளுங்கள்",
            "ml" to "രാവിലെയുള്ള വിവരണം കേൾക്കുക",
            "mr" to "सकाळचा अहवाल ऐका",
            "bn" to "সকালের ব্রিফিং শুনুন"
        ),
        "nav_home" to mapOf(
            "en" to "Home",
            "hi" to "होम",
            "kn" to "ಮುಖಪುಟ",
            "te" to "హోమ్",
            "ta" to "முகப்பு",
            "ml" to "ഹോം",
            "mr" to "मुख्यपृष्ठ",
            "bn" to "হোম"
        ),
        "nav_farms" to mapOf(
            "en" to "My Farms",
            "hi" to "मेरे खेत",
            "kn" to "ನನ್ನ ಹೊಲಗಳು",
            "te" to "నా పొలాలు",
            "ta" to "என் பண்ணைகள்",
            "ml" to "എന്റെ ഫാമുകൾ",
            "mr" to "माझी शेतं",
            "bn" to "আমার খামার"
        ),
        "nav_voice" to mapOf(
            "en" to "Talk",
            "hi" to "बोलें",
            "kn" to "ಮಾತನಾಡಿ",
            "te" to "మాట్లాడు",
            "ta" to "பேசு",
            "ml" to "സംസാരിക്കൂ",
            "mr" to "बोल",
            "bn" to "বলুন"
        ),
        "nav_market" to mapOf(
            "en" to "Market",
            "hi" to "बाजार",
            "kn" to "ಮಾರುಕಟ್ಟೆ",
            "te" to "మార్కెట్",
            "ta" to "சந்தை",
            "ml" to "വിപണി",
            "mr" to "बाजार",
            "bn" to "বাজার"
        ),
        "nav_profile" to mapOf(
            "en" to "Profile",
            "hi" to "प्रोफाइल",
            "kn" to "ಪ್ರೊಫೈಲ್",
            "te" to "ప్రొఫైల్",
            "ta" to "சுயவிவரம்",
            "ml" to "പ്രൊഫൈൽ",
            "mr" to "प्रोफाइल",
            "bn" to "প্রোফাইল"
        )
    )

    fun getString(key: String, languageCode: String = "en"): String {
        return translations[key]?.get(languageCode)
            ?: translations[key]?.get("en")
            ?: key
    }

    fun getLanguage(code: String): SupportedLanguage {
        return languages.firstOrNull { it.code == code } ?: languages[0]
    }
}
