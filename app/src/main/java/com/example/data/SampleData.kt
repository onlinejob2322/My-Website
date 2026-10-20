package com.example.data

import com.example.model.BannerItem
import com.example.model.Product
import com.example.model.ProductCategory

object SampleData {

    val categories = listOf(
        ProductCategory("all", "All Items", "সব প্রোডাক্ট", "grid_view"),
        ProductCategory("smartwatch", "Smartwatch & Tech", "স্মার্টওয়াচ ও টেক", "watch"),
        ProductCategory("audio", "Audio & TWS", "অডিও ও ইয়ারবাডস", "headphones"),
        ProductCategory("perfume", "Oud & Perfume", "উদ ও পারফিউম", "spa"),
        ProductCategory("leather", "Leather Craft", "লেদার কালেকশন", "wallet"),
        ProductCategory("accessories", "Gadgets", "অ্যাকসেসরিজ", "devices"),
        ProductCategory("fashion", "Men's Fashion", "পুরুষের ফ্যাশন", "checkroom")
    )

    val banners = listOf(
        BannerItem(
            id = "ban-001",
            title = "New Luxury Tech & Lifestyle Collection",
            titleBn = "নতুন প্রিমিয়াম টেক ও লাইফস্টাইল কালেকশন",
            subtitle = "Elevate your daily presence with Navy Blue & Gold precision engineering. Fast delivery across Bangladesh!",
            subtitleBn = "সাদা, নেভি ব্লু ও গোল্ডের আভিজাত্যে সেরা সব প্রোডাক্ট কিনুন এখনই। ক্যাশ অন ডেলিভারি ও দ্রুত হোম ডেলিভারি!",
            buttonText = "Shop Collection",
            buttonTextBn = "কালেকশন দেখুন",
            imageUrl = "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=1600&auto=format&fit=crop&q=80",
            badge = "গ্র্যান্ড উদ্বোধন অফার",
            targetCategory = "all"
        ),
        BannerItem(
            id = "ban-002",
            title = "Up to 40% Off on Premium Smartwatches",
            titleBn = "স্মার্টওয়াচে পাচ্ছেন সর্বোচ্চ ৪০% পর্যন্ত ডিসকাউন্ট",
            subtitle = "Always-on AMOLED display, Bluetooth calling, and luxury design. Cash on delivery available everywhere!",
            subtitleBn = "এইচডি কলিং, অ্যামোলেড ডিসপ্লে ও ওয়াটারপ্রুফ টেকনোলজি। আজই অর্ডার করুন!",
            buttonText = "Order Now",
            buttonTextBn = "অর্ডার করুন এখনই",
            imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=1600&auto=format&fit=crop&q=80",
            badge = "লিমিটেড স্টক",
            targetCategory = "smartwatch"
        ),
        BannerItem(
            id = "ban-003",
            title = "Authentic Arabian Oud & French Perfumes",
            titleBn = "খাঁটি অ্যারাবিয়ান উদ ও ফ্র্যাগরেন্স কালেকশন",
            subtitle = "Royal scents crafted to leave an unforgettable impression. 100% genuine guaranteed.",
            subtitleBn = "রাজকীয় সুবাস ও দীর্ঘস্থায়ী আত্মবিশ্বাস। ১০০% অরিজিনাল গ্যারান্টি।",
            buttonText = "Explore Scents",
            buttonTextBn = "সুগন্ধি দেখুন",
            imageUrl = "https://images.unsplash.com/photo-1594035910387-fea47794261f?w=1600&auto=format&fit=crop&q=80",
            badge = "১০০% আসল উদ",
            targetCategory = "perfume"
        )
    )

    val initialProducts = listOf(
        Product(
            id = "prod-001",
            name = "Ultra AMOLED Gold Edition Smart Watch",
            nameBn = "আল্ট্রা অ্যামোলেড গোল্ড এডিশন স্মার্ট ওয়াচ",
            description = "Premium titanium and gold tone chassis with 2.02 inch brilliant AMOLED display. Features HD Bluetooth calling, health tracking sensor suite, 100+ sports modes, IP68 water resistance, and 7-day battery life.",
            descriptionBn = "প্রিমিয়াম টাইটানিয়াম ও গোল্ড টোন চ্যাসিস সহ ২.০২ ইঞ্চি অ্যামোলেড ডিসপ্লে। এইচডি ব্লুটুথ কলিং, হেলথ ট্র্যাকিং সেন্সর, ওয়াটারপ্রুফ এবং ৭ দিনের ব্যাটারি লাইফ।",
            price = 3250.0,
            originalPrice = 4800.0,
            discountPercent = 32,
            category = "smartwatch",
            categoryBn = "স্মার্টওয়াচ ও টেক",
            image = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop&q=80",
            stock = 18,
            rating = 4.9,
            reviewsCount = 142,
            badge = "সেরা অফার",
            isFeatured = true,
            specs = mapOf(
                "Display" to "2.02\" Curved AMOLED Always-On",
                "Battery" to "420mAh (Up to 7 days)",
                "Connectivity" to "Bluetooth 5.3 Low Energy",
                "Water Resistance" to "IP68 Certified",
                "Compatibility" to "Android & iOS Support"
            )
        ),
        Product(
            id = "prod-002",
            name = "Pro ANC Wireless Stereo Earbuds (Navy/Gold)",
            nameBn = "প্রো এএনসি ওয়্যারলেস স্টেরিও ইয়ারবাডস",
            description = "Active Noise Cancellation up to 35dB with spatial audio, 13mm graphene composite dynamic drivers, quad-mic ENC for crystal-clear phone calls, and 36-hour total playback with sleek metallic charging case.",
            descriptionBn = "৩৫ ডেসিবেল পর্যন্ত অ্যাক্টিভ নয়েজ ক্যান্সেলেশন, স্পেশাল অডিও, ক্রিস্টাল ক্লিয়ার কলিং ও মোট ৩৬ ঘণ্টার ব্যাকআপ।",
            price = 2150.0,
            originalPrice = 3200.0,
            discountPercent = 33,
            category = "audio",
            categoryBn = "অডিও ও ইয়ারবাডস",
            image = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=800&auto=format&fit=crop&q=80",
            stock = 24,
            rating = 4.8,
            reviewsCount = 98,
            badge = "টপ সেলার",
            isFeatured = true,
            specs = mapOf(
                "Driver" to "13mm Dynamic Graphene",
                "Noise Reduction" to "35dB Hybrid ANC + ENC",
                "Playtime" to "8h single charge, 36h with case",
                "Charging" to "Type-C Fast Charge (10 min = 2h)"
            )
        ),
        Product(
            id = "prod-003",
            name = "Royal Oud & Amber Luxury Perfume (100ml)",
            nameBn = "রয়্যাল উদ অ্যান্ড অ্যাম্বার লাক্সারি পারফিউম (১০০ মিলি)",
            description = "Intense, captivating blend of aged Cambodian oud, French rose, rich golden amber, and Madagascar vanilla. Formulated with 25% concentrated perfume oil for over 24-hour longevity.",
            descriptionBn = "কম্বোডিয়ান খাঁটি উদ, ফ্রেঞ্চ গোলাপ, গোল্ডেন অ্যাম্বার ও ভ্যানিলার রাজকীয় সুবাস। লং-লাস্টিং পারফিউম যা কাপড়ে থাকবে ২৪ ঘণ্টার বেশি।",
            price = 2450.0,
            originalPrice = 3500.0,
            discountPercent = 30,
            category = "perfume",
            categoryBn = "উদ ও পারফিউম",
            image = "https://images.unsplash.com/photo-1594035910387-fea47794261f?w=800&auto=format&fit=crop&q=80",
            stock = 15,
            rating = 5.0,
            reviewsCount = 84,
            badge = "প্রিমিয়াম চয়েস",
            isFeatured = true,
            specs = mapOf(
                "Volume" to "100ml / 3.4 fl. oz",
                "Concentration" to "Eau de Parfum (25% Oil)",
                "Notes" to "Oud, Amber, French Rose, Vanilla",
                "Origin" to "Direct Import"
            )
        ),
        Product(
            id = "prod-004",
            name = "Genuine Full-Grain Leather Wallet (Navy & Gold Accent)",
            nameBn = "আসল ফুল-গ্রেইন লেদার প্রিমিয়াম ওয়ালেট",
            description = "Handcrafted from 100% genuine top grain cowhide leather. Includes 8 card slots, dual currency compartments, RFID protection lining to prevent unauthorized wireless scans, and gold foil embossed logo.",
            descriptionBn = "১০০% খাঁটি চামড়ায় তৈরি বিলাসবহুল ওয়ালেট। ৮টি কার্ড স্লট, ডাবল কারেন্সি চেম্বার এবং আরএফআইডি স্ক্যান প্রোটেকশন।",
            price = 1350.0,
            originalPrice = 1950.0,
            discountPercent = 31,
            category = "leather",
            categoryBn = "লেদার কালেকশন",
            image = "https://images.unsplash.com/photo-1627123424574-724758594e93?w=800&auto=format&fit=crop&q=80",
            stock = 32,
            rating = 4.7,
            reviewsCount = 65,
            badge = "১০০% চামড়া",
            isFeatured = false,
            specs = mapOf(
                "Material" to "100% Genuine Full-Grain Cow Leather",
                "Slots" to "8 Cards, 2 Cash Chambers, 1 ID Window",
                "Security" to "RFID Signal Blocking Layer",
                "Warranty" to "1 Year Replacement Warranty"
            )
        ),
        Product(
            id = "prod-005",
            name = "22.5W Fast Charging 20,000mAh Power Bank with Digital Display",
            nameBn = "২২.৫ ওয়াট ফাস্ট চার্জিং ২০,০০০ এমএএইচ পাওয়ার ব্যাংক",
            description = "Massive 20,000mAh airline-approved high-density polymer battery. Supports PD 3.0, QC 4.0, and 22.5W Super Charge for iPhone and Android devices. Features real-time precision LED percentage display.",
            descriptionBn = "বিশাল ২০,০০০ এমএএইচ পলিমার ব্যাটারি, পিডি ৩.০ এবং ২২.৫ ওয়াট ফাস্ট চার্জিং। ডিজিটাল এলইডি ডিসপ্লেতে ব্যাটারি পার্সেন্টেজ দেখা যায়।",
            price = 1850.0,
            originalPrice = 2600.0,
            discountPercent = 29,
            category = "accessories",
            categoryBn = "অ্যাকসেসরিজ",
            image = "https://images.unsplash.com/photo-1609592424300-8fa538e12455?w=800&auto=format&fit=crop&q=80",
            stock = 25,
            rating = 4.9,
            reviewsCount = 112,
            badge = "ফাস্ট চার্জ",
            isFeatured = false,
            specs = mapOf(
                "Capacity" to "20,000mAh / 74Wh",
                "Output" to "USB-C PD 20W / USB-A 22.5W QC",
                "Inputs" to "Type-C & Micro-USB",
                "Safety" to "Overheat, short-circuit protection"
            )
        ),
        Product(
            id = "prod-006",
            name = "Executive Navy Blue Polo Shirt with Gold Crest",
            nameBn = "এক্সিকিউটিভ নেভি ব্লু পোলো শার্ট (গোল্ড ক্রাফট)",
            description = "Tailored from 240 GSM 100% combed organic pique cotton with gold metallic thread crest embroidery. Mercerized finish for ultra-soft hand feel, breathability, and zero color fading after washes.",
            descriptionBn = "২৪০ জিএসএম ১০০% অর্গানিক কম্বড কটন ফ্যাব্রিক এবং গোল্ড এমব্রয়ডারি লোগো। আরামদায়ক এবং কালার গ্যারান্টি।",
            price = 1150.0,
            originalPrice = 1650.0,
            discountPercent = 30,
            category = "fashion",
            categoryBn = "পুরুষের ফ্যাশন",
            image = "https://images.unsplash.com/photo-1581655353564-df123a1eb820?w=800&auto=format&fit=crop&q=80",
            stock = 40,
            rating = 4.7,
            reviewsCount = 73,
            badge = "প্রিমিয়াম ফ্যাব্রিক",
            isFeatured = false,
            specs = mapOf(
                "Fabric" to "100% Combed Pique Cotton (240 GSM)",
                "Collar" to "Reinforced Ribbed Collar & Cuffs",
                "Fit" to "Regular Executive Fit",
                "Sizes" to "M, L, XL, XXL"
            )
        ),
        Product(
            id = "prod-007",
            name = "Wireless RGB Gaming & Studio Mechanical Keyboard",
            nameBn = "ওয়্যারলেস আরজিবি মেকানিক্যাল কিবোর্ড",
            description = "Compact 75% layout mechanical keyboard with hot-swappable tactile switches, sound dampening silicone gaskets, multi-device Tri-Mode connectivity (Bluetooth 5.0, 2.4GHz Wireless, USB-C), and dynamic RGB lighting.",
            descriptionBn = "কমপ্যাক্ট ৭৫% মেকানিক্যাল কিবোর্ড, হট-সোয়াপ্যাবল সুইচ, আরজিবি ব্যাকলাইট এবং ওয়্যারলেস/ব্লুটুথ সংযোগ।",
            price = 3650.0,
            originalPrice = 5200.0,
            discountPercent = 30,
            category = "smartwatch",
            categoryBn = "স্মার্টওয়াচ ও টেক",
            image = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&auto=format&fit=crop&q=80",
            stock = 12,
            rating = 4.9,
            reviewsCount = 59,
            badge = "গেমিং গেয়ার",
            isFeatured = false,
            specs = mapOf(
                "Switches" to "Hot-Swappable Custom Tactile Yellow",
                "Modes" to "Bluetooth 5.0 / 2.4Ghz / USB Type-C",
                "Battery" to "4000mAh Rechargeable Lithium",
                "Lighting" to "16.8M Per-Key RGB with 20 effects"
            )
        ),
        Product(
            id = "prod-008",
            name = "Luxury Aviator Sunglasses with UV400 Polarized Lens",
            nameBn = "লাক্সারি অ্যাভিয়েটর সানগ্লাস (ইউভি৪০০ পোলারাইজড)",
            description = "Classic gold metal alloy frame with midnight navy polarized lenses. Blocks 100% of UVA and UVB harmful rays with anti-scratch coating and comfortable silicone nose pads.",
            descriptionBn = "গোল্ড অ্যালয় ফ্রেম এবং ইউভি৪০০ পোলারাইজড লেন্স। চোখ সুরক্ষিত রাখতে ১০০% ক্ষতিকর রোদ প্রতিরোধক।",
            price = 990.0,
            originalPrice = 1500.0,
            discountPercent = 34,
            category = "fashion",
            categoryBn = "পুরুষের ফ্যাশন",
            image = "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=800&auto=format&fit=crop&q=80",
            stock = 30,
            rating = 4.6,
            reviewsCount = 47,
            badge = "ইউভি প্রোটেকশন",
            isFeatured = false,
            specs = mapOf(
                "Lens" to "TAC Polarized UV400 Protection",
                "Frame" to "Corrosion-Resistant Gold Alloy",
                "Weight" to "24g Ultra Lightweight",
                "Includes" to "Leather Hard Case & Cleaning Cloth"
            )
        )
    )

    // Pre-populate sample order for immediate order tracking demonstration
    val sampleInitialOrders = listOf(
        com.example.model.Order(
            id = "ABD-10842",
            customerName = "Tanvir Ahmed",
            phone = "01755123456",
            address = "House 24, Road 7, Sector 3, Uttara",
            district = "Dhaka",
            deliveryArea = com.example.model.DeliveryArea.INSIDE_DHAKA,
            paymentMethod = com.example.model.PaymentMethod.COD,
            items = listOf(
                com.example.model.OrderItemRecord(
                    productId = "prod-001",
                    productName = "Ultra AMOLED Gold Edition Smart Watch",
                    productNameBn = "আল্ট্রা অ্যামোলেড গোল্ড এডিশন স্মার্ট ওয়াচ",
                    price = 3250.0,
                    quantity = 1,
                    image = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop&q=80"
                )
            ),
            subtotal = 3250.0,
            deliveryFee = 0.0, // Free delivery because > 2500
            discount = 0.0,
            total = 3250.0,
            status = com.example.model.OrderStatus.SHIPPED,
            courierName = "Steadfast Courier",
            courierTrackingCode = "STDF-998240",
            createdAt = System.currentTimeMillis() - 86400000L // 1 day ago
        )
    )
}
