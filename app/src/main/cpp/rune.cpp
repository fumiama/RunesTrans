#include <jni.h>
#include <string>
#include <cctype>
const char *list = reinterpret_cast<const char *>("ᛆᛒᚴᛑᛂᚠᚵᚼᛁᚤᚴᛚᛙᚿᚮᛔᛩᚱᛌᛐᚢᚡᚥᛪᚤᛎ᛫᛭");
using namespace std;
void cp(char *str, char no){
    str[0] = list[no * 3];
    str[1] = list[no * 3 + 1];
    str[2] = list[no * 3 + 2];
    str[3] = 0;
}
char fnd(const char *p){
    char i;
    for(i = 0; i < 28; i++) if(p[2] == list[i * 3 + 2]) break;
    return (i == 26)?' ':((i == 27)?'+':(char)(i + 'a'));
}
extern "C" JNIEXPORT jstring JNICALL
Java_top_fumiama_runestrans_MainActivity_turn(
        JNIEnv* env,
        jobject obj,
        jstring inpstr) {
    const char *strr = env->GetStringUTFChars(inpstr, JNI_FALSE);
    if(!strr){
        env->ReleaseStringUTFChars(inpstr, strr);
        return env->NewStringUTF("");
    }
    const char *str = strr;
    char * const out = new char[strlen(str) * 3 + 1];
    int i = 0;
    for(i = 0; *str; str++){
        if(isalpha(*str)){
            cp(out + i, static_cast<char>(tolower(*str) - 'a'));
            i += 3;
        }else if(*str == ' '){
            cp(out + i, 26);
            i += 3;
        }else if(*str == '+') {
            cp(out + i, 27);
            i += 3;
        }else if((unsigned char)*str == 0xE1 && ((unsigned char)str[1] == 0x9A || (unsigned char)str[1] == 0x9B)){
            out[i] = fnd(str);
            i++;
            str += 2;
        }else{
            out[i] = *str;
            i++;
        }
    }
    out[i] = 0;
    env->ReleaseStringUTFChars(inpstr, strr);
    return env->NewStringUTF(out);
}
