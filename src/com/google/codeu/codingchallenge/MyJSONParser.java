// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class MyJSONParser implements JSONParser {

  @Override
  public JSON parse(String in) throws IOException
  {
    String originalString = in;//To save the original text
    Map<String,String> keyValue = new HashMap<>();//Map for key-value pair
    Map<String,String> keyObject = new HashMap<>();//Map for key-object pair
    final String invalidMessage = "Invalid JSON Text";
    in = in.trim();//Remove spaces both side
    int lastPrintableString;//Get the last printable String
    for(lastPrintableString = in.length()-1;lastPrintableString>=0;lastPrintableString--)
    {
      if(in.charAt(lastPrintableString) =='\n'||in.charAt(lastPrintableString)=='\b'||in.charAt(lastPrintableString)==' ')
      {
        continue;
      }
      else
      {
        break;
      }
    }
    int firstPrintableString;//Get the first printable string
    for(firstPrintableString = 0;firstPrintableString<in.length();firstPrintableString++)
    {
      if(in.charAt(firstPrintableString) =='\n'||in.charAt(firstPrintableString)=='\b'||in.charAt(firstPrintableString)==' ')
      {
        continue;
      }
      else
      {
        break;
      }
    }
    in = in.substring(firstPrintableString,lastPrintableString+1);
    if(in.charAt(0)!='{'||in.charAt(in.length()-1)!='}')//Check if its start and end are qualified
    {
      throw  new IOException(invalidMessage);
    }
    String innerString = in.substring(1,in.lastIndexOf("}"));//Get innerString
    innerString = innerString.trim();
    innerString = innerString.replaceAll("\n","");
    innerString = innerString.replaceAll("\t","");//Remove other non-printable characters
    // Spaces cannot be removed right now, they might be in the quotation mark for key and value
    if(innerString.isEmpty())//Empty JSON
    {
      return new MyJSON();
    }

    int currentPos = 0;//Current Position
    boolean firstChar = false;//First character should start with "\""
    while(currentPos < innerString.length())//For all pairs in the array
    {
      char currentChar = innerString.charAt(currentPos);//check if it is qualified
      if(currentChar==' ')
      {
        currentPos++;
        continue;
      }
      else if(!firstChar)//It is not fist character, it should start with ","  next time
      {
        firstChar = true;
      }
      else if(currentChar == ',')
      {
        currentPos++;
      }
      else
      {
        throw new IOException(invalidMessage);//other invalid character
      }
      for(int i = currentPos;i<innerString.length();i++)
      {
        currentChar = innerString.charAt(i);
        if(currentChar==' ')//Remove other space characters
        {
          continue;
        }
        else if(currentChar == '\"' )// key should be started with quotation mark
        {
          currentPos = i;
          break;
        }
        else//Illegal character
        {
          throw new IOException(invalidMessage);
        }
      }
      String key;
      String value;
      if(innerString.charAt(currentPos)=='\"')
      {
        try {
          int i;
          for (i = currentPos + 1; innerString.charAt(i) != '\"'; i++) ;//find the right quotation mark
          key = innerString.substring(currentPos + 1, i);//Set current value to key
          currentPos = i+1;
          currentChar = innerString.charAt(currentPos);
          while(currentChar==' ')//Remove space characters
          {
            currentChar = innerString.charAt(++currentPos);
            continue;
          }
          if(innerString.charAt(currentPos)==':')
          {
            currentPos++;
            currentChar = innerString.charAt(currentPos);
            while(currentChar==' ')//Remove space characters
            {
              currentChar = innerString.charAt(++currentPos);
              continue;
            }
            if(innerString.charAt(currentPos)=='{')//object
            {
              int posRightBracket = innerString.lastIndexOf("}");//,currentPos);
              if(posRightBracket!=-1)
              {
                value = innerString.substring(currentPos,posRightBracket+1);
                currentPos = posRightBracket + 1;
                keyObject.put(key,value);//set value to key-object pair
                continue;
              }
              else
              {
                throw new IOException(invalidMessage);
              }
            }
            else if(innerString.charAt(currentPos)=='\"')//value
            {
              currentPos++;
              int posRightQuoMark = innerString.indexOf('\"',currentPos);
              if(posRightQuoMark!=-1)
              {
                value = innerString.substring(currentPos,posRightQuoMark);
                currentPos = posRightQuoMark+1;
                keyValue.put(key,value);//set value to key-value pair
                continue;
              }
              else
              {
                throw new IOException(invalidMessage);//other exception
              }
            }
            else
            {
              throw new IOException(invalidMessage);
            }
          }
          else
          {
            throw new IOException(invalidMessage);
          }
        }
        catch (Exception e)
        {
          throw new IOException(invalidMessage);
        }
      }
      else
      {
        throw new IOException(invalidMessage);
      }
    }
    return new MyJSON(keyValue,keyObject,originalString);
  }
}
