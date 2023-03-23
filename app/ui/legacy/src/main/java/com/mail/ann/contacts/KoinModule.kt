package com.mail.ann.contacts

import org.koin.dsl.module

val contactsModule = module {
    single { ContactLetterExtractor() }
    factory { ContactLetterBitmapConfig(context = get(), themeManager = get()) }
    factory { ContactLetterBitmapCreator(letterExtractor = get(), config = get()) }
    factory { ContactPhotoLoader(contentResolver = get(), contacts = get()) }
    factory { ContactPictureLoader(context = get(), contactLetterBitmapCreator = get()) }
    factory { ContactImageBitmapDecoderFactory(contactPhotoLoader = get()) }
}
